const $ = (id) => document.getElementById(id);

const esc = (value) => String(value ?? '')
  .replaceAll('&', '&amp;')
  .replaceAll('<', '&lt;')
  .replaceAll('>', '&gt;')
  .replaceAll('"', '&quot;')
  .replaceAll("'", '&#039;');

const API = {
  async call(url, opts = {}) {
    const token = localStorage.getItem('ainexusToken');
    opts.headers = {
      ...(opts.headers || {}),
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {})
    };

    const res = await fetch(url, opts);
    let data = {};
    try { data = await res.json(); } catch (_) {}

    if (res.status === 401) {
      localStorage.removeItem('ainexusToken');
      localStorage.removeItem('ainexusUser');
      if (!location.pathname.endsWith('login.html')) location.href = 'login.html';
    }
    if (!res.ok) throw new Error(data.message || `Request failed (${res.status})`);
    return data;
  }
};

const Auth = {
  async login(event) {
    event.preventDefault();
    const msg = $('msg');
    msg.textContent = '';
    try {
      const data = await API.call('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email: $('email').value.trim(), password: $('password').value })
      });
      localStorage.setItem('ainexusToken', data.token);
      localStorage.setItem('ainexusUser', JSON.stringify(data.user));
      location.href = 'app.html';
    } catch (error) {
      msg.textContent = error.message;
    }
  },

  async register(event) {
    event.preventDefault();
    const msg = $('msg');
    msg.textContent = '';
    try {
      const data = await API.call('/api/auth/register', {
        method: 'POST',
        body: JSON.stringify({
          name: $('name').value.trim(),
          email: $('email').value.trim(),
          password: $('password').value
        })
      });
      localStorage.setItem('ainexusToken', data.token);
      localStorage.setItem('ainexusUser', JSON.stringify(data.user));
      location.href = 'app.html';
    } catch (error) {
      msg.textContent = error.message;
    }
  }
};

const App = {
  user: null,
  team: null,
  project: null,

  init() {
    const token = localStorage.getItem('ainexusToken');
    if (!token) {
      location.href = 'login.html';
      return;
    }

    this.user = JSON.parse(localStorage.getItem('ainexusUser') || 'null');
    $('sideName').textContent = this.user?.name || 'Student';
    $('sideRole').textContent = this.user?.role || 'STUDENT';
    $('avatar').textContent = (this.user?.name || 'A')[0].toUpperCase();

    if (['ADMIN', 'FACULTY'].includes(this.user?.role)) $('adminNav').classList.remove('hidden');
    $('logout').onclick = () => this.logout();
    document.querySelectorAll('#nav button').forEach(button => {
      button.onclick = () => this.show(button.dataset.page);
    });
    this.show('overview');
  },

  async logout() {
    try { await API.call('/api/auth/logout', { method: 'POST' }); } catch (_) {}
    localStorage.removeItem('ainexusToken');
    localStorage.removeItem('ainexusUser');
    location.href = 'login.html';
  },

  async show(page) {
    document.querySelectorAll('#nav button').forEach(button =>
      button.classList.toggle('active', button.dataset.page === page));

    const titles = {
      overview: 'Overview', project: 'Project & Reviews', team: 'Team workspace',
      recommend: 'Recommendation Engine', quantum: 'Quantum Lab', network: 'Network Lab',
      history: 'History', admin: 'Faculty Review'
    };
    $('pageTitle').textContent = titles[page] || 'Workspace';

    try {
      await this[page]();
    } catch (error) {
      this.toast(error.message, true);
    }
  },

  async overview() {
    const [team, project, history] = await Promise.all([
      API.call('/api/teams/me'), API.call('/api/projects/me'), API.call('/api/recommendations/history')
    ]);
    this.currentTeam = team;
    this.currentProject = project;
    const status = project.exists ? project.status.replaceAll('_', ' ') : 'NOT STARTED';

    $('page').innerHTML = `
      <div class="hero-panel">
        <div>
          <div class="eyebrow">YOUR CONTROL CENTER</div>
          <h2>Build something that <span>stands out.</span></h2>
          <p>Manage your Java project, submit reviews, explore labs and generate technical recommendations.</p>
          <button class="primary" onclick="App.show('project')">Open project →</button>
        </div>
        <div class="metric-orb"><small>PROJECT STATUS</small><b>${esc(status)}</b><span>${esc(team.hasTeam ? team.name : 'No team yet')}</span></div>
      </div>
      <div class="stat-grid">
        <div class="stat"><small>TEAM</small><b>${team.hasTeam ? `${team.members.length}/4` : '—'}</b><span>${team.hasTeam ? 'members' : 'Create or join'}</span></div>
        <div class="stat"><small>REVIEW</small><b>${project.exists ? esc(project.status.replace('REVIEW_', 'R')) : 'R0'}</b><span>Current stage</span></div>
        <div class="stat"><small>ANALYSES</small><b>${history.length}</b><span>Recommendation runs</span></div>
      </div>
      <div class="quick-grid">
        <button onclick="App.show('recommend')"><b>✦ Analyze a problem</b><span>Get a technology path</span></button>
        <button onclick="App.show('quantum')"><b>◈ Open Quantum Lab</b><span>Run a circuit simulation</span></button>
        <button onclick="App.show('network')"><b>⌁ Open Network Lab</b><span>Model network performance</span></button>
      </div>`;
  },

  async team() {
    const [team, invitations] = await Promise.all([
      API.call('/api/teams/me'), API.call('/api/teams/invitations')
    ]);
    this.currentTeam = team;

    $('page').innerHTML = `
      <div class="page-grid">
        <div class="panel">
          <div class="panel-head">
            <div><div class="eyebrow">COLLABORATION</div><h2>${team.hasTeam ? esc(team.name) : 'Create your team'}</h2></div>
            <span class="pill">${esc(team.hasTeam ? team.code : '3–4 members')}</span>
          </div>
          ${team.hasTeam ? `
            <div class="members">${team.members.map(member => `
              <div class="member"><div class="avatar">${esc(member.name[0])}</div><div><b>${esc(member.name)}</b><small>${esc(member.email)}</small></div><span>${esc(member.role)}</span></div>
            `).join('')}</div>
            ${team.leaderId === this.user.id && team.members.length < 4 ? `
              <div class="inline-form"><input id="inviteEmail" type="email" placeholder="Registered member email"><button class="primary" onclick="App.invite()">Invite</button></div>
              <small class="form-hint">Invite registered students until your team reaches 4 members.</small>
            ` : ''}
          ` : `
            <p>Create the team first. A valid submission team must contain 3–4 members.</p>
            <div class="inline-form"><input id="teamName" maxlength="100" placeholder="Team name e.g. Quantum Stack"><button class="primary" onclick="App.createTeam()">Create team</button></div>
          `}
        </div>
        <div class="panel">
          <div class="eyebrow">PENDING INVITATIONS</div>
          <h3>${invitations.length ? `${invitations.length} waiting` : 'No pending invitations'}</h3>
          ${invitations.map(inv => `
            <div class="invite"><div><b>${esc(inv.team)}</b><small>Leader: ${esc(inv.leader)} • ${esc(inv.code)}</small></div>
              <div><button class="accept" onclick="App.respond(${inv.id},true)">Accept</button><button class="decline" onclick="App.respond(${inv.id},false)">Decline</button></div>
            </div>
          `).join('')}
        </div>
      </div>`;
  },

  async createTeam() {
    const name = $('teamName').value.trim();
    if (!name) return this.toast('Enter a team name.', true);
    await API.call('/api/teams', { method: 'POST', body: JSON.stringify({ name }) });
    this.toast('Team created. Invite at least 2 more members.');
    await this.show('team');
  },

  async invite() {
    const email = $('inviteEmail').value.trim();
    if (!email) return this.toast('Enter a member email.', true);
    await API.call('/api/teams/invite', { method: 'POST', body: JSON.stringify({ email }) });
    this.toast('Invitation sent.');
    await this.show('team');
  },

  async respond(id, accept) {
    await API.call(`/api/teams/invitations/${id}/respond?accept=${accept}`, { method: 'POST' });
    this.toast(accept ? 'Invitation accepted.' : 'Invitation declined.');
    await this.show('team');
  },

  async project() {
    const team = await API.call('/api/teams/me');
    this.currentTeam = team;
    let project = await API.call('/api/projects/me');
    this.currentProject = project;

    const canCreate = !project.exists && team.hasTeam && team.members.length >= 3 && team.members.length <= 4;
    const can0 = project.exists && project.status === 'REVIEW_0_PENDING';
    const can1 = project.exists && project.status === 'REVIEW_1_OPEN';
    const can2 = project.exists && project.status === 'REVIEW_2_OPEN';
    const canEdit = project.exists && project.status === 'REVIEW_0_PENDING';

    $('page').innerHTML = `
      <div class="panel">
        <div class="panel-head"><div><div class="eyebrow">JAVA PROJECT SUBMISSION</div><h2>Project board</h2></div><span class="pill">${esc(project.exists ? project.status.replaceAll('_', ' ') : 'NOT CREATED')}</span></div>
        <p class="muted">Team: ${esc(project.exists ? project.team : (team.hasTeam ? team.name : 'Create a team first'))}</p>
        ${project.exists && !canEdit ? `<div class="project-summary"><b>${esc(project.title)}</b><span>${esc(project.description)}</span></div>` : ''}
        ${!project.exists && !canCreate ? `<div class="locked">Create a team and add at least 3 members before creating the project.</div>` : ''}
        ${(canCreate || canEdit) ? `<div class="form-grid"><label>Project title<input id="ptitle" maxlength="200" value="${esc(project.title)}" placeholder="Your project title"></label><label>Description<textarea id="pdesc" maxlength="3000" placeholder="Problem, solution, users and key features">${esc(project.description)}</textarea></label></div><button class="primary" onclick="App.saveProject()">Save project details →</button>` : ''}
      </div>
      <div class="review-grid">
        ${this.reviewCard(project, 0, can0, 'Title & description', 'Approval gate • No marks', 'Short project description for faculty approval')}
        ${this.reviewCard(project, 1, can1, 'Project progress', '33 marks • Deadline 10 Oct 2026', 'Progress, architecture, implementation and evidence')}
        ${this.reviewCard(project, 2, can2, 'Final evaluation', '17 marks • Deadline 15 Nov 2026', 'Final implementation, testing, results and conclusion')}
      </div>`;
  },

  reviewCard(project, number, open, title, subtitle, placeholder) {
    return `<div class="review-card ${open ? 'open' : ''}">
      <span>REVIEW ${number}</span><h3>${title}</h3><p>${subtitle}</p>
      ${open ? `<textarea id="r${number}" maxlength="5000" placeholder="${placeholder}"></textarea><button class="primary" onclick="App.submitReview(${number})">Submit Review ${number}</button>` : this.reviewInfo(project, number)}
    </div>`;
  },

  reviewInfo(project, number) {
    const review = project.reviews?.find(item => item.reviewNo === number);
    if (!review) return `<div class="locked">🔒 Complete the previous stage to unlock.</div>`;
    return `<div class="submitted"><b>${esc(review.status)}</b><span>${review.marks} marks</span><small>${esc(review.feedback || 'Awaiting faculty feedback.')}</small></div>`;
  },

  async saveProject() {
    const title = $('ptitle').value.trim();
    const description = $('pdesc').value.trim();
    if (!title || !description) return this.toast('Project title and description are required.', true);
    await API.call('/api/projects/me', { method: 'POST', body: JSON.stringify({ title, description }) });
    this.toast('Project details saved.');
    await this.show('project');
  },

  async submitReview(number) {
    const content = $(`r${number}`).value.trim();
    if (!content) return this.toast('Write review evidence before submitting.', true);
    await API.call('/api/projects/reviews', { method: 'POST', body: JSON.stringify({ reviewNo: number, content }) });
    this.toast(`Review ${number} submitted for faculty approval.`);
    await this.show('project');
  },

  async recommend() {
    $('page').innerHTML = `
      <div class="panel wide"><div class="eyebrow">INTELLIGENT ANALYSIS</div><h2>Describe the problem. We’ll map the path.</h2>
      <p class="muted">A deterministic, explainable engine scores AI, Quantum and Network approaches from workload signals.</p>
      <div class="form-grid">
        <label>Problem title<input id="qtitle" maxlength="200" placeholder="e.g. Large-scale anomaly detection"></label>
        <label>Problem type<select id="qtype"><option value="DATA">Data / ML</option><option value="OPTIMIZATION">Optimization</option><option value="NETWORK">Networking</option><option value="GENERAL">General computing</option></select></label>
        <label class="fullrow">Description<textarea id="qdesc" maxlength="5000" placeholder="Describe your workload, scale, constraints and desired outcome..."></textarea></label>
        <label>Dataset size<select id="qsize"><option>SMALL</option><option>MEDIUM</option><option>VERY_LARGE</option></select></label>
        <label>Priority<select id="qpriority"><option>NORMAL</option><option>HIGH</option></select></label>
        <label>Budget<select id="qbudget"><option>LOW</option><option selected>MEDIUM</option><option>HIGH</option></select></label>
      </div>
      <button class="primary" onclick="App.runRecommendation()">Analyze requirement →</button><div id="recResult"></div></div>`;
  },

  async runRecommendation() {
    const title = $('qtitle').value.trim();
    const description = $('qdesc').value.trim();
    if (!title || !description) return this.toast('Problem title and description are required.', true);
    const data = await API.call('/api/recommendations/analyze', {
      method: 'POST', body: JSON.stringify({
        title, description, problemType: $('qtype').value,
        datasetSize: $('qsize').value, priority: $('qpriority').value, budget: $('qbudget').value
      })
    });
    $('recResult').innerHTML = `<div class="result"><div><small>RECOMMENDED PATH</small><h2>${esc(data.technology)}</h2><p>${esc(data.reason)}</p></div><div class="confidence"><b>${Math.round(data.confidence * 100)}%</b><span>confidence</span></div></div>
      <div class="scorebars">${Object.entries(data.scores).map(([key, value]) => `<div><span>${esc(key)}</span><div><i style="width:${Math.min(Number(value),100)}%"></i></div><b>${Math.round(value)}</b></div>`).join('')}</div>`;
  },

  async quantum() {
    $('page').innerHTML = `<div class="lab-layout"><div class="panel"><div class="eyebrow">QUANTUM SIMULATOR</div><h2>Build a small circuit.</h2>
      <p class="muted">Educational state-vector simulator for 1–8 qubits. Each selected H or X gate is applied to every qubit.</p>
      <div class="form-grid"><label>Qubits<select id="qubits"><option>1</option><option selected>2</option><option>3</option><option>4</option><option>5</option><option>6</option><option>7</option><option>8</option></select></label>
      <label>Gate sequence<input id="gates" value="H" placeholder="H, X, H"></label></div>
      <button class="primary" onclick="App.runQuantum()">Run simulation →</button><div id="qresult"></div></div>
      <div class="panel visual-panel"><div class="quantum-visual"><div class="qcircle">|0⟩</div><div class="qline"></div><div class="qcircle">H/X</div><div class="qline"></div><div class="qcircle">ψ</div></div><p>Probabilities are calculated from the simulated state vector.</p></div></div>`;
  },

  async runQuantum() {
    const raw = $('gates').value.split(',').map(x => x.trim().toUpperCase()).filter(Boolean);
    if (!raw.length) return this.toast('Enter at least one gate: H or X.', true);
    const data = await API.call('/api/simulations/quantum', {
      method: 'POST', body: JSON.stringify({ qubits: Number($('qubits').value), gates: raw })
    });
    $('qresult').innerHTML = `<div class="result compact"><div><small>SIMULATION COMPLETE</small><h3>${data.qubits} qubits • ${data.gates.join(' + ')}</h3><p>${esc(data.note)}</p></div></div>
      <div class="bars">${data.probabilities.map((probability, index) => `<div class="barrow"><span>|${index.toString(2).padStart(data.qubits, '0')}⟩</span><div><i style="width:${Math.max(3, probability * 100)}%"></i></div><b>${Math.round(probability * 100)}%</b></div>`).join('')}</div>`;
  },

  async network() {
    $('page').innerHTML = `<div class="lab-layout"><div class="panel"><div class="eyebrow">NETWORK LAB</div><h2>Model network performance.</h2>
      <p class="muted">Estimate throughput, packet loss and jitter from topology, scale, bandwidth and latency.</p>
      <div class="form-grid"><label>Topology<select id="topology"><option>STAR</option><option>RING</option><option>MESH</option><option>TREE</option><option>BUS</option></select></label>
      <label>Nodes<input id="nodes" type="number" min="2" max="100" value="12"></label><label>Bandwidth (Mbps)<input id="bandwidth" type="number" min="1" max="100000" value="100"></label><label>Latency (ms)<input id="latency" type="number" min="0" max="10000" value="20"></label></div>
      <button class="primary" onclick="App.runNetwork()">Run network simulation →</button><div id="nresult"></div></div>
      <div class="panel topology-card"><div class="network-map"><span></span><span></span><span></span><span></span><span></span><b>CORE</b></div><p>Topology preview • metrics are generated by the simulation engine.</p></div></div>`;
  },

  async runNetwork() {
    const nodes = Number($('nodes').value);
    const bandwidth = Number($('bandwidth').value);
    const latency = Number($('latency').value);
    if (!Number.isFinite(nodes) || !Number.isFinite(bandwidth) || !Number.isFinite(latency)) return this.toast('Enter valid numeric values.', true);
    const data = await API.call('/api/simulations/network', {
      method: 'POST', body: JSON.stringify({ topology: $('topology').value, nodes, bandwidth, latency })
    });
    $('nresult').innerHTML = `<div class="metric-grid">${[
      ['Throughput', `${data.estimatedThroughputMbps} Mbps`], ['Packet loss', `${data.estimatedPacketLossPercent}%`],
      ['Jitter', `${data.jitterMs} ms`], ['Latency', `${data.latencyMs} ms`]
    ].map(([label, value]) => `<div class="stat"><small>${label}</small><b>${value}</b></div>`).join('')}</div>`;
  },

  async history() {
    const [recommendations, simulations] = await Promise.all([
      API.call('/api/recommendations/history'), API.call('/api/simulations/history')
    ]);
    $('page').innerHTML = `<div class="panel"><div class="eyebrow">ACTIVITY</div><h2>Recent analyses & simulations.</h2>
      ${recommendations.length ? '<h3>Recommendation history</h3>' : ''}
      ${recommendations.map(item => `<div class="history-item"><span>✦</span><div><b>${esc(item.technology)}</b><small>${Math.round(item.confidence * 100)}% confidence • ${esc(item.createdAt)}</small></div></div>`).join('')}
      ${simulations.length ? simulations.map(item => `<div class="history-item"><span>${item.type === 'QUANTUM' ? '◈' : '⌁'}</span><div><b>${esc(item.type)} simulation</b><small>${esc(item.createdAt)}</small></div></div>`).join('') : '<p class="muted">No simulations yet.</p>'}
    </div>`;
  },

  async admin() {
    if (!['ADMIN', 'FACULTY'].includes(this.user.role)) {
      return this.toast('Faculty/admin access required.', true);
    }
    const projects = await API.call('/api/admin/projects');
    $('page').innerHTML = `<div class="panel"><div class="eyebrow">FACULTY CONTROL</div><h2>Review queue</h2><p class="muted">Approve submitted reviews to unlock the next stage. Marks are constrained to the official 33 + 17 rubric.</p>
      ${projects.length ? projects.map(project => `<div class="admin-row"><div><b>${esc(project.title)}</b><small>${esc(project.team)} • ${esc(project.status)}</small></div><div>
        ${(project.reviews || []).filter(review => review.status === 'SUBMITTED').map(review => `<button class="accept" onclick="App.approve(${project.id},${review.reviewNo})">Approve R${review.reviewNo}</button>`).join('') || '<small>Nothing awaiting approval</small>'}
      </div></div>`).join('') : '<div class="locked">No projects have been submitted yet.</div>'}
    </div>`;
  },

  async approve(id, reviewNo) {
    const maxMarks = reviewNo === 1 ? 33 : reviewNo === 2 ? 17 : 0;
    const feedback = window.prompt(`Faculty feedback for Review ${reviewNo}:`, '') ?? '';
    if (feedback.length > 2000) return this.toast('Feedback cannot exceed 2000 characters.', true);
    await API.call(`/api/admin/projects/${id}/reviews/${reviewNo}/approve?marks=${maxMarks}&feedback=${encodeURIComponent(feedback)}`, { method: 'POST' });
    this.toast(`Review ${reviewNo} approved.`);
    await this.show('admin');
  },

  toast(message, error = false) {
    const toast = $('toast');
    toast.textContent = message;
    toast.className = error ? 'error show' : 'show';
    clearTimeout(this.toastTimer);
    this.toastTimer = setTimeout(() => { toast.className = ''; }, 3000);
  }
};
