document.addEventListener('DOMContentLoaded', function () {
    loadAssets();
});

function loadAssets() {
    fetch('/api/assets')
        .then(response => response.json())
        .then(data => {
            const warehouseSection = document.querySelector('.column:nth-child(1)');
            const agvSection = document.querySelector('.column:nth-child(2)');
            const assemblySection = document.querySelector('.column:nth-child(3)');

            warehouseSection.innerHTML = '<h2>Warehouse</h2>';
            agvSection.innerHTML = '<h2>AGV</h2>';
            assemblySection.innerHTML = '<h2>Assembly Station</h2>';

            data.forEach(asset => {
                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <p>ID: ${asset.id}</p>
                    <p>Name: ${asset.name}</p>
                    <p>Type: ${asset.type}</p>
                    <p>State: ${asset.state}</p>
                    <button>+</button>
                `;

                if (asset.type === 'WAREHOUSE') {
                    warehouseSection.appendChild(card);
                } else if (asset.type === 'AGV') {
                    agvSection.appendChild(card);
                } else if (asset.type === 'ASSEMBLY_STATION') {
                    assemblySection.appendChild(card);
                }
            });

            const addWarehouseButton = document.createElement('button');
            addWarehouseButton.className = 'add-button';
            addWarehouseButton.textContent = '+';
            warehouseSection.appendChild(addWarehouseButton);

            const addAgvButton = document.createElement('button');
            addAgvButton.className = 'add-button';
            addAgvButton.textContent = '+';
            agvSection.appendChild(addAgvButton);

            const addAssemblyButton = document.createElement('button');
            addAssemblyButton.className = 'add-button';
            addAssemblyButton.textContent = '+';
            assemblySection.appendChild(addAssemblyButton);
        })
        .catch(error => console.error('Error loading assets:', error));
}
