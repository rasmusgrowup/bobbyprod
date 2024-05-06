document.getElementById("actionButton").addEventListener("click", function() {
    fetch('/start', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                // Handle success
                console.log('Action performed successfully');
            } else {
                // Handle error
                console.error('Failed to perform action');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
