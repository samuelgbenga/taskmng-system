document.addEventListener('DOMContentLoaded', function() {
    const taskTableBody = document.querySelector('#taskTable tbody');
    const statusFilter = document.getElementById('statusFilter');
    const priorityFilter = document.getElementById('priorityFilter');
    let allTasks = []; // To store all tasks fetched initially

    // Function to fetch all tasks from backend
    async function fetchTasks() {
        try {
            const response = await fetch('http://localhost:8080/api/tasks/get-all-task', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token') // Assuming token is stored in localStorage
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch tasks');
            }

            allTasks = await response.json();
            populateTable(allTasks); // Populate table with fetched tasks

        } catch (error) {
            console.error('Error fetching tasks:', error);
            alert('Error fetching tasks. Please try again.');
        }
    }

    // Function to populate table with tasks
    function populateTable(tasks) {
        taskTableBody.innerHTML = ''; // Clear existing table rows

        tasks.forEach(task => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${task.taskResponseInfo.title}</td>
                <td>${task.taskResponseInfo.description}</td>
                <td>${task.taskResponseInfo.deadline}</td>
                <td>${task.taskResponseInfo.priorityLevel}</td>
                <td>${task.taskResponseInfo.status}</td>
                <td>
                    <a href="editTask.html?taskId=${task.taskResponseInfo.id}" class="edit-btn">Edit</a>
                    <a href="#" class="delete-btn" data-task-id="${task.taskResponseInfo.id}">Delete</a>
                </td>
            `;
            taskTableBody.appendChild(row);
        });

        // Add event listeners to dynamically created delete buttons
        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(button => {
            button.addEventListener('click', async function(event) {
                event.preventDefault();
                const taskId = button.getAttribute('data-task-id');
                await deleteTask(taskId);
            });
        });
    }

    // Function to delete a task
    async function deleteTask(taskId) {
        if (confirm('Are you sure you want to delete this task?')) {
            try {
                const response = await fetch(`http://localhost:8080/api/tasks/delete-task/${taskId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('Token') // Assuming token is stored in localStorage
                    }
                });

                if (response.ok) {
                    // Remove the task row from the table
                    fetchTasks(); // Refresh the table after deletion
                } else {
                    console.error('Failed to delete task:', response.statusText);
                }

                alert('Task deleted successfully!');
            } catch (error) {
                console.error('Error deleting task:', error);
                alert('Error deleting task. Please try again.');
            }
        }
    }

    // Initial fetch and populate tasks on page load
    fetchTasks();
});
