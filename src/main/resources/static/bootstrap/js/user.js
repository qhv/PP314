window.addEventListener('DOMContentLoaded', getPrincipal);

async function getPrincipal() {
    const result = await fetch('/api/v1/principal');
    const principal = await result.json();
    const roles = principal.roles.map(role => role.name).reduce((acc, name) => acc + ' ' + name);

    if (roles.includes('ADMIN')) {
        window.location.replace('/admin?user');
    }

    const rowValues = document.getElementById('userPage').children;

    rowValues[0].textContent = principal.id;
    rowValues[1].textContent = principal.firstName;
    rowValues[2].textContent = principal.lastName;
    rowValues[3].textContent = principal.age;
    rowValues[4].textContent = principal.login;
    rowValues[5].textContent = roles;
}
