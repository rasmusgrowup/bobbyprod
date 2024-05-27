function updateAssetsContainer(assets) {
    // Get the parent HTML element. Replace 'assetContainer' with the actual ID of your HTML element.
    const container = document.getElementById('assetContainer');

    // Clear the existing content.
    container.innerHTML = '';

    // Loop through the assets array.
    assets.forEach(asset => {
        // Create a new div element.
        const div = document.createElement('div');
        div.style.marginLeft = '2rem';

        // Set the content of the div.
        div.textContent = `"Name": { "${asset.name}", "State:" "${asset.state}" }`;

        // Append the div to the container.
        container.appendChild(div);
    });
}

function updateQueueContainer(products) {
    // Get the parent HTML element. Replace 'assetContainer' with the actual ID of your HTML element.
    const container = document.getElementById('queueContainer');

    // Clear the existing content.
    container.innerHTML = '';

    // Loop through the assets array.
    products.forEach(product => {
        // Create a new div element.
        const div = document.createElement('div');
        div.style.marginLeft = '2rem';

        // Set the content of the div.
        div.textContent = `"Name": { "${product.name}", "ID": "${product.id}", "Status:" "${product.status}" }`;

        // Append the div to the container.
        container.appendChild(div);
    });
}

function updateInProgressContainer(products) {
    // Get the parent HTML element. Replace 'assetContainer' with the actual ID of your HTML element.
    const container = document.getElementById('productsInProgressContainer');

    // Clear the existing content.
    container.innerHTML = '';

    // Loop through the assets array.
    products.forEach(product => {
        // Create a new div element.
        const div = document.createElement('div');
        div.style.marginLeft = '2rem';

        // Set the content of the div.
        div.textContent = `"Name": { "${product.name}", "Status:" "${product.status}" }`;

        // Append the div to the container.
        container.appendChild(div);
    });
}

function updateFinishedProductsContainer(products) {
    // Get the parent HTML element. Replace 'assetContainer' with the actual ID of your HTML element.
    const container = document.getElementById('finishedProductsContainer');

    // Clear the existing content.
    container.innerHTML = '';

    // Loop through the assets array.
    products.forEach(product => {
        // Create a new div element.
        const div = document.createElement('div');
        div.style.marginLeft = '2rem';

        // Set the content of the div.
        div.textContent = `"Name": { "${product.name}", "Id:" "${product.id}" }`;

        // Append the div to the container.
        container.appendChild(div);
    });
}

function fetchAssets() {
    fetch('/api/assets', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(assets => {
            updateAssetsContainer(assets);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function fetchProductionQueue() {
    fetch('/api/production-queue', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(products => {
            updateQueueContainer(products);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function fetchActiveProducts() {
    fetch('/api/active-products', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(products => {
            updateInProgressContainer(products);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function fetchFinishedProducts() {
    fetch('/api/finished-products', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(products => {
            updateFinishedProductsContainer(products);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function update() {
    fetchAssets();
    fetchProductionQueue();
    fetchFinishedProducts();
}
// Fetch assets when the page loads
setInterval(update, 500);

document.getElementById("startProduction").addEventListener("click", function() {
    fetch('/api/start-production', {
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

document.getElementById("addProductButton").addEventListener("click", function() {
    fetch('/api/add-product', {
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