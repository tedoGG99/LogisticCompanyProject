function toggleInputs() {
    const select = document.getElementById('deliveryTypeSelect');
    const selectedOption = select.options[select.selectedIndex];
    
    // Read the "data-requires-office" attribute we generated with Thymeleaf
    // Note: dataset strings are "true" or "false"
    const requiresOffice = selectedOption.dataset.requiresOffice === 'true';

    const officeSection = document.getElementById('officeSection');
    const addressSection = document.getElementById('addressSection');
    const officeInput = document.getElementById('officeSelect');
    const addressInput = document.getElementById('addressInput');

    if (requiresOffice) {
        // Show Office, Hide Address
        officeSection.classList.remove('hidden');
        addressSection.classList.add('hidden');
        
        // Add required attribute so browser validates it
        officeInput.setAttribute('required', 'required');
        addressInput.removeAttribute('required');
    } else {
        // Show Address, Hide Office
        officeSection.classList.add('hidden');
        addressSection.classList.remove('hidden');
        
        officeInput.removeAttribute('required');
        addressInput.setAttribute('required', 'required');
    }
}

// Run once on page load to set correct state
document.addEventListener("DOMContentLoaded", function() {
    toggleInputs();
});