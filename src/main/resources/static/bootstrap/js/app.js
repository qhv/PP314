window.addEventListener('DOMContentLoaded', getAllUsers);
window.addEventListener('DOMContentLoaded', getAllRoles);
window.addEventListener('DOMContentLoaded', getPrincipal);

async function getAllUsers() {
    const result = await fetch('/api/v1/admin/users');
    const users = await result.json();

    users.forEach(user => addUserToTable(user));
}

function addUserToTable({id, firstName, lastName, age, login, roles}) {
    const userTable = document.getElementById("users");

    userTable.insertAdjacentHTML('beforeend', `
        <tr id="user${id}">
          <td>${id}</td>
          <td>${firstName}</td>
          <td>${lastName}</td>
          <td>${age == null ? '' : age}</td>
          <td>${login}</td>
          <td>
            ${roles.map(role => role.name.replace('ROLE_', ''))
              .reduce((name, nextName) => name + ' ' + nextName)}
          </td>
          <td>
            <a class="btn text-white kata-bg-17a2b8" data-bs-toggle="modal" data-bs-target="#editUser">Edit</a>
          </td>
          <td>
            <a class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteUser">Delete</a>
          </td>
        </tr>
    `);
}

function updateUserInTable({id, firstName, lastName, age, login, roles}) {
    const rowNodes = document.getElementById(`user${id}`).children;
    rowNodes[0].textContent = id;
    rowNodes[1].textContent = firstName;
    rowNodes[2].textContent = lastName;
    rowNodes[3].textContent = age;
    rowNodes[4].textContent = login;
    rowNodes[5].textContent = roles.map(role => role.name.replace('ROLE_', ''))
        .reduce((acc, name) => acc + ' ' + name);
}

async function getAllRoles() {
    const result = await fetch('/api/v1/admin/roles');
    const roles = await result.json();
    const shortRoles = roles.map(role => ({id: role.id, name: role.name.replace('ROLE_', '')}));

    const createCheckbox = document.getElementById('createCheckbox');
    const updateCheckbox = document.getElementById('updateCheckbox');
    const deleteCheckbox = document.getElementById('deleteCheckbox');

    shortRoles.forEach(role => {
        addActiveCheckbox(createCheckbox, 'create', role);
        addActiveCheckbox(updateCheckbox, 'update', role);
        addInactiveCheckbox(deleteCheckbox, 'delete', role.name);
    });
}

function addActiveCheckbox(node, operation, role) {
    node.insertAdjacentHTML('beforeend', `
        <input id="${operation}${role.name}" name="${operation}Roles" data-role-id="${role.id}"
            data-role-name="${role.name}" value="${role.name}" class="btn-check" type="checkbox">
        <label class="btn btn-outline-secondary kata-btn-primary border-0 rounded-0 px-1 py-0 w-100 text-start"
            for="${operation}${role.name}">${role.name}</label>
   `);
}

function addInactiveCheckbox(node, operation, roleName) {
    node.insertAdjacentHTML('beforeend', `
        <input id="${operation}${roleName}" name="${operation}Roles" value="${roleName}"
            class="btn-check" type="checkbox" disabled>
        <label class="btn btn-outline-secondary border-0 rounded-0 px-1 py-0 w-100 text-start"
            for="${operation}${roleName}">${roleName}</label>
    `);
}

async function getPrincipal() {
    const result = await fetch('/api/v1/principal');
    const principal = await result.json();
    const rowValues = document.getElementById('userPage').children;

    rowValues[0].textContent = principal.id;
    rowValues[1].textContent = principal.firstName;
    rowValues[2].textContent = principal.lastName;
    rowValues[3].textContent = principal.age;
    rowValues[4].textContent = principal.login;
    rowValues[5].textContent = principal.roles.map(role => role.name.replace('ROLE_', ''))
        .reduce((acc, name) => acc + ' ' + name);
}

document.getElementById('users').addEventListener('click', event => {
    if (event.target.tagName === 'A') {
        const userValues = event.target.parentElement.parentElement.children;
        const rolesStr = userValues[5].textContent;

        if (event.target.dataset.bsTarget === '#deleteUser') {
            const form = document.getElementById('deleteUserForm');
            form.deleteId.value = userValues[0].textContent;
            form.deleteFirstname.value = userValues[1].textContent;
            form.deleteLastname.value = userValues[2].textContent;
            form.deleteAge.value = userValues[3].textContent;
            form.deleteLogin.value = userValues[4].textContent;
            form.deleteRoles.forEach(node => node.checked = rolesStr.includes(node.value));
        } else {
            const form = document.getElementById('editUserForm');
            form.editId.value = userValues[0].textContent;
            form.editFirstname.value = userValues[1].textContent;
            form.editLastname.value = userValues[2].textContent;
            form.editAge.value = userValues[3].textContent;
            form.editLogin.value = userValues[4].textContent;
            form.updateRoles.forEach(node => node.checked = rolesStr.includes(node.value));
        }
    }
});

document.getElementById('addNewUser').addEventListener('click', async () => {
    const firstNameEl = document.getElementById('firstname');
    const lastNameEl = document.getElementById('lastname');
    const ageEl = document.getElementById('age');
    const loginEl = document.getElementById('login');
    const passwordEl = document.getElementById('rawPassword');
    const rolesEl = document.getElementsByName('createRoles');
    const roles = Array.from(rolesEl)
        .filter(role => role.checked)
        .map(role => ({id: role.dataset.roleId, name: role.dataset.roleName}));

    const csrfToken = document.querySelector("[name='_csrf']");

    const result = await fetch('api/v1/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken.value
        },
        body: JSON.stringify({
            firstName: firstNameEl.value,
            lastName: lastNameEl.value,
            age: ageEl.value,
            login: loginEl.value,
            password: passwordEl.value,
            roles: roles
        })
    });

    const user = await result.json();
    addUserToTable(user);

    firstNameEl.value = '';
    lastNameEl.value = '';
    ageEl.value = '';
    loginEl.value = '';
    passwordEl.value = '';
    rolesEl.forEach(role => role.checked = false);
    document.getElementById('userTableLink').click();
})

document.getElementById('editUserForm').addEventListener('submit', async event => {
    event.preventDefault();

    const editModal = document.getElementById('editUser');
    const form = event.target;
    const idEl = form.editId;
    const firstNameEl = form.editFirstname;
    const lastNameEl = form.editLastname;
    const ageEl = form.editAge;
    const loginEl = form.editLogin;
    const passwordEl = form.editPassword;
    const rolesEl = form.updateRoles;
    const roles = Array.from(rolesEl)
        .filter(role => role.checked)
        .map(role => ({id: role.dataset.roleId, name: role.dataset.roleName}));

    console.log(roles);

    const csrfToken = document.querySelector("[name='_csrf']");

    const result = await fetch(`api/v1/admin/users/${idEl.value}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken.value
        },
        body: JSON.stringify({
            id: idEl.value,
            firstName: firstNameEl.value,
            lastName: lastNameEl.value,
            age: ageEl.value,
            login: loginEl.value,
            password: passwordEl.value,
            roles: roles
        })
    });

    bootstrap.Modal.getInstance(editModal).hide();

    const user = await result.json();

    updateUserInTable(user);
    form.reset();
})

document.getElementById('deleteUserForm').addEventListener('submit', async event => {
    const id = event.target[0].value;
    const deleteModal = document.getElementById('deleteUser');
    const csrfToken = document.querySelector("[name='_csrf']");

    event.preventDefault();

    const result = await fetch(`api/v1/admin/users/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken.value
        }
    });

    bootstrap.Modal.getInstance(deleteModal).hide();

    document.getElementById(`user${id}`).remove();
})