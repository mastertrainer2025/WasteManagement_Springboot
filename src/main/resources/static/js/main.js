// Eco-Waste Portal JS Utils & Segregation Game
document.addEventListener('DOMContentLoaded', () => {
    initSegregationGame();
    initImageUploadPreview();
});

// 1. Waste Segregation Game Logic
function initSegregationGame() {
    const gameItem = document.getElementById('game-item');
    if (!gameItem) return;

    const bins = document.querySelectorAll('.game-bin');
    const scoreVal = document.getElementById('game-score');
    const feedback = document.getElementById('game-feedback');

    const items = [
        { name: 'Apple Peel', type: 'ORGANIC' },
        { name: 'Plastic Bottle', type: 'RECYCLABLE' },
        { name: 'Car Battery', type: 'HAZARDOUS' },
        { name: 'Broken Sofa', type: 'BULKY' },
        { name: 'Banana Skin', type: 'ORGANIC' },
        { name: 'Cardboard Box', type: 'RECYCLABLE' },
        { name: 'Spray Paint Can', type: 'HAZARDOUS' },
        { name: 'Old Refrigerator', type: 'BULKY' },
        { name: 'Coffee Grounds', type: 'ORGANIC' },
        { name: 'Glass Jar', type: 'RECYCLABLE' },
        { name: 'Expired Medicine', type: 'HAZARDOUS' },
        { name: 'Broken Mattress', type: 'BULKY' }
    ];

    let currentItemIndex = 0;
    let score = 0;

    // Load first item
    loadNextItem();

    // Drag start
    gameItem.addEventListener('dragstart', (e) => {
        e.dataTransfer.setData('text/plain', items[currentItemIndex].type);
        gameItem.classList.add('dragging');
    });

    gameItem.addEventListener('dragend', () => {
        gameItem.classList.remove('dragging');
    });

    // Bin event listeners
    bins.forEach(bin => {
        bin.addEventListener('dragover', (e) => {
            e.preventDefault();
            bin.classList.add('drag-over');
        });

        bin.addEventListener('dragleave', () => {
            bin.classList.remove('drag-over');
        });

        bin.addEventListener('drop', (e) => {
            e.preventDefault();
            bin.classList.remove('drag-over');
            
            const itemType = e.dataTransfer.getData('text/plain');
            const binType = bin.dataset.type;

            if (itemType === binType) {
                score += 10;
                scoreVal.textContent = score;
                feedback.innerHTML = '<span class="text-success fw-bold">✓ Correct! Well done!</span>';
            } else {
                feedback.innerHTML = `<span class="text-danger fw-bold">✗ Oops! That goes in the ${binType.toLowerCase()} bin.</span>`;
            }

            currentItemIndex = (currentItemIndex + 1) % items.length;
            loadNextItem();
        });
    });

    function loadNextItem() {
        const item = items[currentItemIndex];
        gameItem.textContent = item.name;
    }
}

// 2. Collection Staff Image Preview and Base64 Converter
function initImageUploadPreview() {
    const fileInput = document.getElementById('collectionImageFile');
    const hiddenInput = document.getElementById('collectionProofImageBase64');
    const imagePreview = document.getElementById('collectionImagePreview');

    if (!fileInput || !hiddenInput) return;

    fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (event) => {
                const base64String = event.target.result;
                hiddenInput.value = base64String;
                
                if (imagePreview) {
                    imagePreview.src = base64String;
                    imagePreview.classList.remove('d-none');
                }
            };
            reader.readAsDataURL(file);
        }
    });
}
