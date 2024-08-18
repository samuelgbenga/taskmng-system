document.addEventListener('DOMContentLoaded', function() {
    const taskId = new URLSearchParams(window.location.search).get('taskId');
    const taskTitle = document.getElementById('taskTitle');
    const taskDescription = document.getElementById('taskDescription');
    const taskDeadline = document.getElementById('taskDeadline');
    const taskPriority = document.getElementById('taskPriority');
    const taskStatus = document.getElementById('taskStatus');
    const editTaskLink = document.getElementById('editTaskLink');

    // Check if token exists in localStorage
    if (!localStorage.getItem('Token')) {
        // Redirect to login page if token is missing
        window.location.href = '/auth/login.html';
        return;
    }

    fetchUserProfile();
    document.querySelector('.logout a').addEventListener('click', logout);

    // Function to fetch task details
    async function fetchTaskDetails(taskId) {
        try {
            const response = await fetch(`http://localhost:8080/api/tasks/get-task/${taskId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token') // Assuming token is stored in localStorage
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch task details');
            }

            const task = await response.json();
            updateTaskDetails(task.taskResponseInfo);
        } catch (error) {
            console.error('Error fetching task details:', error);
            alert('Error fetching task details. Please try again.');
        }
    }

    // Function to update task details on the page
    function updateTaskDetails(task) {
        taskTitle.textContent = task.title;
        taskDescription.textContent = task.description;
        taskDeadline.textContent = formatDate(task.deadline);
        taskPriority.textContent = task.priorityLevel;
        taskStatus.textContent = task.status;
        editTaskLink.href = `editTask.html?taskId=${task.id}`;
    }

    // Function to format date (assuming deadline is in ISO format)
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
    }

    // Fetch user profile
    async function fetchUserProfile() {
        try {
            const response = await fetch('http://localhost:8080/api/user/profile', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch user profile');
            }

            const userProfile = await response.json();
            console.log("user profile", userProfile);
            updateWelcomeMessage(userProfile.userProfileResponseInfo.firstName);
        } catch (error) {
            console.error('Error fetching user profile:', error);
            // Handle error if needed
        }
    }

    // Function to update welcome message
    function updateWelcomeMessage(firstName) {
        const welcomeMessage = document.querySelector('.welcome-message');
        if (welcomeMessage) {
            welcomeMessage.textContent = `Welcome ${firstName}!`;
        }
    }

    // Function to handle logout
    async function logout() {
        try {
            const response = await fetch('http://localhost:8080/api/auth/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                }
            });

            if (!response.ok) {
                throw new Error('Failed to logout');
            }

            // Clear localStorage and redirect to login page
            localStorage.removeItem('Token');
            window.location.href = '/auth/login.html';
        } catch (error) {
            console.error('Error logging out:', error);
            // Handle error if needed
        }
    }

    // Fetch task details when the page loads
    fetchTaskDetails(taskId);
});
