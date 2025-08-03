// Function to filter job listings by client name
function filterProjects() {
    const searchInput = document.getElementById("search-bar").value.toLowerCase();
    const jobCards = document.querySelectorAll(".job-card");

    jobCards.forEach(job => {
        const clientName = job.getAttribute("data-client").toLowerCase();
        if (clientName.includes(searchInput)) {
            job.style.display = "block"; // Show matching jobs
        } else {
            job.style.display = "none"; // Hide non-matching jobs
        }
    });
}
