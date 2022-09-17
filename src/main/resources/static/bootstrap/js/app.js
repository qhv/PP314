window.addEventListener('DOMContentLoaded', getAllUsers);

async function getAllUsers() {
    const result = await fetch('/api/v1/admin/users');
    const users = await result.json();

    users.forEach(user => userToTable(user));
}

function userToTable({id, firstName, lastName, age, login, roles}) {
    const userTable = document.getElementById("users");
    console.log(roles);

    userTable.insertAdjacentHTML('beforeend', `
        <tr id="user${id}">
          <td>${id}</td>
          <td>${firstName}</td>
          <td>${lastName}</td>
          <td>${age}</td>
          <td>${login}</td>
          <td>${roles.map(role => role.name).reduce((name, nextName) => name + ' ' + nextName)}</td>
          <td>
            <a class="btn text-white kata-bg-17a2b8" data-bs-toggle="modal" data-bs-target="#editUser${id}">Edit</a>
          </td>
          <td>
            <a class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteUser">Delete</a>
          </td>
        </tr>
    `);
}

document.getElementById('users').addEventListener('click', event => {
    if (event.target.tagName === 'A') {
        if (event.target.dataset.bsTarget === '#deleteUser') {
            const userValues = event.target.parentElement.parentElement.children;
            const rolesStr = userValues[5].textContent;
            const form = document.getElementById('deleteUserForm');
            form.deleteId.value = userValues[0].textContent;
            form.deleteFirstname.value = userValues[1].textContent;
            form.deleteLastname.value = userValues[2].textContent;
            form.deleteAge.value = userValues[3].textContent;
            form.deleteLogin.value = userValues[4].textContent;
            form.deleteRoles.forEach(node => node.checked = rolesStr.includes(node.value));
        }
    }
});

document.getElementById('addNewUser').addEventListener('click', async () => {
    const firstNameEl = document.getElementById('firstname');
    const lastNameEl = document.getElementById('lastname');
    const ageEl = document.getElementById('age');
    const loginEl = document.getElementById('login');
    const passwordEl = document.getElementById('rawPassword');
    const rolesEl = document.getElementsByName('selectedRoles');
    const roles = Array.from(rolesEl)
        .filter(role => role.checked)
        .map(role => {
            return {
                id: role.dataset.roleId,
                name: role.dataset.roleName
            };
        });

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
    userToTable(user);

    firstNameEl.value = '';
    lastNameEl.value = '';
    ageEl.value = '';
    loginEl.value = '';
    passwordEl.value = '';
    rolesEl.forEach(role => role.checked = false);
    document.getElementById('userTableLink').click();
})

document.getElementById('deleteUserForm').addEventListener('submit', async event => {
    const id = event.target[0].value;
    const deleteModal = document.getElementById('deleteUser');
    const csrfToken = document.querySelector("[name='_csrf']");

    event.preventDefault();

    await fetch(`api/v1/admin/users/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken.value
        }
    });

    bootstrap.Modal.getInstance(deleteModal).hide();

    document.getElementById(`user${id}`).remove();
})