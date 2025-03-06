document.addEventListener("DOMContentLoaded", function () {
    const userList = document.getElementById("user-list");
    const page1Button = document.getElementById("page1");
    const page2Button = document.getElementById("page2");
    const userDetails = document.getElementById("user-details");
    const sortSelect = document.getElementById("sort");
    let users = [];

    function fetchUsers(page) {
        fetch(`https://reqres.in/api/users?page=${page}`)
            .then(response => response.json())
            .then(data => {
                users = data.data;
                renderUsers(users);
            })
            .catch(error => console.error("Ошибка загрузки пользователей:", error));
    }

    function renderUsers(userArray) {
        userList.innerHTML = "";
        userArray.forEach(user => {
            const li = document.createElement("li");
            li.classList.add("item");
            li.dataset.id = user.id;
            li.innerHTML = `
                <span class="name">${user.first_name}</span> 
                <span class="surname">${user.last_name}</span>
                <button class="delete-btn">Удалить</button>
            `;
            userList.appendChild(li);
        });
    }

    function fetchUserDetails(id) {
        fetch(`https://reqres.in/api/users/${id}`)
            .then(response => response.json())
            .then(data => {
                const user = data.data;
                userDetails.innerHTML = `
                    <h3>${user.first_name} ${user.last_name}</h3>
                    <p>Email: ${user.email}</p>
                    <img src="${user.avatar}" alt="${user.first_name}">
                `;
            })
            .catch(error => console.error("Ошибка загрузки информации о пользователе:", error));
    }

    userList.addEventListener("click", function (event) {
        const item = event.target.closest(".item");
        if (item) {
            const userId = item.dataset.id;
            if (event.target.classList.contains("delete-btn")) {
                item.remove();

                const elIndexToRemove = users.findIndex(item => item.id == userId);
                users.splice(elIndexToRemove, 1);

                console.log(users);
            } else {
                fetchUserDetails(userId);
            }
        }
    });

    sortSelect.addEventListener("change", function () {
        const sortBy = sortSelect.value;
        users.sort((a, b) => a[sortBy].localeCompare(b[sortBy]));
        renderUsers(users);
    });

    page1Button.addEventListener("click", () => fetchUsers(1));
    page2Button.addEventListener("click", () => fetchUsers(2));

    fetchUsers(1);
});
