/**
 * Library Management System - Client Side Interactions
 */
document.addEventListener('DOMContentLoaded', function () {
    // Mobile sidebar toggle
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }

    // Login preview password visibility toggle
    const passwordToggle = document.querySelector('[data-password-toggle]');
    if (passwordToggle) {
        const passwordInput = document.getElementById(passwordToggle.getAttribute('aria-controls'));
        if (passwordInput) {
            passwordToggle.addEventListener('click', function () {
                const isPassword = passwordInput.type === 'password';
                passwordInput.type = isPassword ? 'text' : 'password';
                passwordToggle.setAttribute('aria-label', isPassword ? 'Hide password' : 'Show password');
                passwordToggle.innerHTML = isPassword
                    ? '<i class="bi bi-eye-slash"></i>'
                    : '<i class="bi bi-eye"></i>';
            });
        }
    }

    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            try {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                bsAlert.close();
            } catch (e) {
                // Ignore if already dismissed
            }
        }, 6000);
    });

    // Delete confirmation modal handler
    const confirmDeleteModal = document.getElementById('confirmDeleteModal');
    if (confirmDeleteModal) {
        confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const actionUrl = button.getAttribute('data-action');
            const itemName = button.getAttribute('data-name') || 'this record';
            const deleteMessage = confirmDeleteModal.querySelector('#deleteModalMessage');
            const deleteForm = confirmDeleteModal.querySelector('#deleteModalForm');

            if (deleteMessage) {
                deleteMessage.textContent = `Are you sure you want to permanently delete "${itemName}"? This action cannot be undone.`;
            }
            if (deleteForm && actionUrl) {
                deleteForm.setAttribute('action', actionUrl);
            }
        });
    }

    // Return book confirmation modal handler
    const returnBookModal = document.getElementById('returnBookModal');
    if (returnBookModal) {
        returnBookModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const actionUrl = button.getAttribute('data-action');
            const bookTitle = button.getAttribute('data-title') || 'this book';
            const memberName = button.getAttribute('data-member') || 'member';
            const returnMessage = returnBookModal.querySelector('#returnModalMessage');
            const returnForm = returnBookModal.querySelector('#returnModalForm');

            if (returnMessage) {
                returnMessage.textContent = `Confirm return of "${bookTitle}" from ${memberName}? This will restore +1 copy to the available catalog.`;
            }
            if (returnForm && actionUrl) {
                returnForm.setAttribute('action', actionUrl);
            }
        });
    }
});
