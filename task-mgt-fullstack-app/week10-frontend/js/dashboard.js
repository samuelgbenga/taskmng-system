document.addEventListener('DOMContentLoaded', function () {
    console.log("JavaScript loaded"); // Check if the script is loaded
    const hamburgerMenu = document.querySelector('.hamburger-menu');
    const navbar = document.querySelector('.navbar');
    const closeIcon = document.querySelector('.close-icon');
    const content = document.querySelector('.content');

    // Function to handle window resize
    function handleResize() {
        if (window.innerWidth > 768) {
            navbar.classList.remove('active');
            hamburgerMenu.style.display = 'none';
            closeIcon.style.display = 'none';
            content.classList.remove('shifted');
        } else {
            if (!navbar.classList.contains('active')) {
                hamburgerMenu.style.display = 'block';
            }
        }
    }

    // Open sidebar when hamburger menu is clicked
    hamburgerMenu.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent the document click event
        console.log("Hamburger menu clicked");
        navbar.classList.add('active');
        hamburgerMenu.style.display = 'none';
        closeIcon.style.display = 'block';
        content.classList.add('shifted');
    });

    // Close sidebar when close icon is clicked
    closeIcon.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent the document click event
        console.log("Close icon clicked");
        navbar.classList.remove('active');
        hamburgerMenu.style.display = 'block';
        closeIcon.style.display = 'none';
        content.classList.remove('shifted');
    });

    // Close sidebar when clicking outside of it
    document.addEventListener('click', function (event) {
        if (!sidebar.contains(event.target) && event.target !== hamburgerMenu && event.target !== closeIcon) {
            console.log("Clicked outside sidebar");
            navbar.classList.remove('active');
            hamburgerMenu.style.display = 'block';
            closeIcon.style.display = 'none';
            content.classList.remove('shifted');
        }
    });

    // Add resize event listener
    window.addEventListener('resize', handleResize);

    // Initial check on page load
    handleResize();
});
