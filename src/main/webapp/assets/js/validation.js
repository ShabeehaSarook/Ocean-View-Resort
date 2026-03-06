(function () {
  function isBlank(value) {
    return !value || !value.trim();
  }

  function getOrCreateMessageBox(form) {
    var box = form.querySelector('.form-message');
    if (!box) {
      box = document.createElement('p');
      box.className = 'form-message notice notice-error';
      form.appendChild(box);
    }
    return box;
  }

  function showError(form, message) {
    var box = getOrCreateMessageBox(form);
    box.textContent = message;
  }

  function clearError(form) {
    var box = form.querySelector('.form-message');
    if (box) {
      box.remove();
    }
  }

  function showFieldError(input, message) {
    clearFieldError(input);
    var error = document.createElement('div');
    error.className = 'field-error';
    error.textContent = message;
    error.setAttribute('role', 'alert');
    input.parentNode.appendChild(error);
    input.setAttribute('aria-invalid', 'true');
  }

  function clearFieldError(input) {
    var parent = input.parentNode;
    var existing = parent.querySelector('.field-error');
    if (existing) {
      existing.remove();
    }
    input.removeAttribute('aria-invalid');
  }

  function showFieldSuccess(input, message) {
    clearFieldError(input);
    var success = document.createElement('div');
    success.className = 'field-success';
    success.textContent = message || 'Valid';
    input.parentNode.appendChild(success);
  }

  function setMinDate() {
    var today = new Date();
    var year = today.getFullYear();
    var month = String(today.getMonth() + 1).padStart(2, '0');
    var day = String(today.getDate()).padStart(2, '0');
    return year + '-' + month + '-' + day;
  }

  function setMaxDate(daysFromNow) {
    var today = new Date();
    today.setDate(today.getDate() + daysFromNow);
    var year = today.getFullYear();
    var month = String(today.getMonth() + 1).padStart(2, '0');
    var day = String(today.getDate()).padStart(2, '0');
    return year + '-' + month + '-' + day;
  }

  function validateRegisterForm(form) {
    var username = form.querySelector('input[name="username"]');
    var password = form.querySelector('input[name="password"]');
    var confirmPassword = form.querySelector('input[name="confirmPassword"]');
    var checkIn = form.querySelector('input[name="checkIn"]');
    var checkOut = form.querySelector('input[name="checkOut"]');

    var usernamePattern = /^[A-Za-z0-9._-]{4,50}$/;
    var phonePattern = /^[+0-9()\-\s]{7,20}$/;
    var phone = form.querySelector('input[name="phone"]');

    if (username && !usernamePattern.test(username.value || '')) {
      showError(form, 'Username must be 4-50 chars and only use letters, numbers, dot, underscore, or hyphen.');
      return false;
    }

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      showError(form, 'Password and confirmation do not match.');
      return false;
    }

    if (password) {
      var v = password.value || '';
      if (!(/[A-Z]/.test(v) && /[a-z]/.test(v) && /\d/.test(v) && v.length >= 8)) {
        showError(form, 'Password must be at least 8 characters and include uppercase, lowercase, and a digit.');
        return false;
      }
    }

    if (phone && !phonePattern.test(phone.value || '')) {
      showError(form, 'Phone format is invalid.');
      return false;
    }

    if (checkIn && checkOut && !isBlank(checkIn.value) && !isBlank(checkOut.value)) {
      if (checkOut.value <= checkIn.value) {
        showError(form, 'Check-out date must be after check-in date.');
        return false;
      }
    }

    clearError(form);
    return true;
  }

  function validateReservationForm(form) {
    var checkIn = form.querySelector('input[name="checkIn"]');
    var checkOut = form.querySelector('input[name="checkOut"]');
    var adults = form.querySelector('input[name="adults"]');
    var children = form.querySelector('input[name="children"]');
    var roomId = form.querySelector('select[name="roomId"]');

    var today = new Date();
    today.setHours(0, 0, 0, 0);

    if (!roomId || isBlank(roomId.value)) {
      showError(form, 'Please select a room.');
      return false;
    }

    if (!checkIn || isBlank(checkIn.value)) {
      showError(form, 'Check-in date is required.');
      return false;
    }

    if (!checkOut || isBlank(checkOut.value)) {
      showError(form, 'Check-out date is required.');
      return false;
    }

    var checkInDate = new Date(checkIn.value);
    var checkOutDate = new Date(checkOut.value);

    if (checkInDate < today) {
      showError(form, 'Check-in date cannot be in the past.');
      showFieldError(checkIn, 'Cannot be in the past');
      return false;
    }

    if (checkOutDate <= checkInDate) {
      showError(form, 'Check-out date must be after check-in date.');
      showFieldError(checkOut, 'Must be after check-in');
      return false;
    }

    var daysDiff = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
    if (daysDiff > 365) {
      showError(form, 'Maximum stay duration is 365 days.');
      showFieldError(checkOut, 'Maximum 365 days stay');
      return false;
    }

    if (daysDiff < 1) {
      showError(form, 'Minimum stay duration is 1 day.');
      showFieldError(checkOut, 'Minimum 1 day stay');
      return false;
    }

    if (adults && Number(adults.value) < 1) {
      showError(form, 'At least 1 adult is required.');
      showFieldError(adults, 'Minimum 1 adult');
      return false;
    }

    if (adults && Number(adults.value) > 20) {
      showError(form, 'Maximum 20 adults allowed.');
      showFieldError(adults, 'Maximum 20 adults');
      return false;
    }

    if (children && Number(children.value) < 0) {
      showError(form, 'Children cannot be negative.');
      showFieldError(children, 'Cannot be negative');
      return false;
    }

    if (children && Number(children.value) > 20) {
      showError(form, 'Maximum 20 children allowed.');
      showFieldError(children, 'Maximum 20 children');
      return false;
    }

    var totalGuests = Number(adults.value) + Number(children.value);
    if (totalGuests > 30) {
      showError(form, 'Maximum total guests is 30.');
      return false;
    }

    clearError(form);
    clearFieldError(checkIn);
    clearFieldError(checkOut);
    clearFieldError(adults);
    clearFieldError(children);
    return true;
  }

  function bindForms(selector, validator) {
    document.querySelectorAll(selector).forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!validator(form)) {
          event.preventDefault();
        }
      });
    });
  }

  function validateBillingForm(form) {
    var subtotal = form.querySelector('input[name="subtotal"]');
    var tax = form.querySelector('input[name="tax"]');
    var discount = form.querySelector('input[name="discount"]');
    var reservationId = form.querySelector('select[name="reservationId"]');

    if (reservationId && isBlank(reservationId.value)) {
      showError(form, 'Please select a reservation.');
      return false;
    }

    if (!subtotal || isBlank(subtotal.value)) {
      showError(form, 'Subtotal is required.');
      showFieldError(subtotal, 'Required');
      return false;
    }

    if (Number(subtotal.value) < 0) {
      showError(form, 'Subtotal cannot be negative.');
      showFieldError(subtotal, 'Cannot be negative');
      return false;
    }

    if (Number(subtotal.value) > 1000000) {
      showError(form, 'Subtotal cannot exceed $1,000,000.');
      showFieldError(subtotal, 'Too large');
      return false;
    }

    if (tax && Number(tax.value) < 0) {
      showError(form, 'Tax cannot be negative.');
      showFieldError(tax, 'Cannot be negative');
      return false;
    }

    if (discount && Number(discount.value) < 0) {
      showError(form, 'Discount cannot be negative.');
      showFieldError(discount, 'Cannot be negative');
      return false;
    }

    var totalValue = Number(subtotal.value) + Number(tax.value || 0) - Number(discount.value || 0);
    if (totalValue < 0) {
      showError(form, 'Total amount cannot be negative. Reduce the discount.');
      showFieldError(discount, 'Discount too high');
      return false;
    }

    clearError(form);
    clearFieldError(subtotal);
    clearFieldError(tax);
    clearFieldError(discount);
    return true;
  }

  function validateRoomForm(form) {
    var roomNumber = form.querySelector('input[name="roomNumber"]');
    var floor = form.querySelector('input[name="floor"]');

    if (!roomNumber || isBlank(roomNumber.value)) {
      showError(form, 'Room number is required.');
      showFieldError(roomNumber, 'Required');
      return false;
    }

    var roomNumPattern = /^[A-Za-z0-9-]{1,20}$/;
    if (!roomNumPattern.test(roomNumber.value.trim())) {
      showError(form, 'Room number must be 1-20 characters (letters, numbers, hyphens only).');
      showFieldError(roomNumber, 'Invalid format');
      return false;
    }

    if (floor && (Number(floor.value) < -5 || Number(floor.value) > 200)) {
      showError(form, 'Floor must be between -5 and 200.');
      showFieldError(floor, 'Invalid floor');
      return false;
    }

    clearError(form);
    clearFieldError(roomNumber);
    clearFieldError(floor);
    return true;
  }

  function validateRoomTypeForm(form) {
    var name = form.querySelector('input[name="name"]');
    var basePrice = form.querySelector('input[name="basePrice"]');
    var capacity = form.querySelector('input[name="capacity"]');

    if (!name || isBlank(name.value)) {
      showError(form, 'Room type name is required.');
      showFieldError(name, 'Required');
      return false;
    }

    if (name.value.trim().length > 100) {
      showError(form, 'Room type name must be 100 characters or less.');
      showFieldError(name, 'Too long');
      return false;
    }

    if (!basePrice || isBlank(basePrice.value)) {
      showError(form, 'Base price is required.');
      showFieldError(basePrice, 'Required');
      return false;
    }

    if (Number(basePrice.value) < 0) {
      showError(form, 'Base price cannot be negative.');
      showFieldError(basePrice, 'Cannot be negative');
      return false;
    }

    if (Number(basePrice.value) > 100000) {
      showError(form, 'Base price cannot exceed $100,000.');
      showFieldError(basePrice, 'Too high');
      return false;
    }

    if (!capacity || isBlank(capacity.value)) {
      showError(form, 'Capacity is required.');
      showFieldError(capacity, 'Required');
      return false;
    }

    if (Number(capacity.value) < 1 || Number(capacity.value) > 50) {
      showError(form, 'Capacity must be between 1 and 50.');
      showFieldError(capacity, 'Invalid capacity');
      return false;
    }

    clearError(form);
    clearFieldError(name);
    clearFieldError(basePrice);
    clearFieldError(capacity);
    return true;
  }

  function initRealTimeValidation() {
    document.querySelectorAll('input[name="checkIn"]').forEach(function (input) {
      input.setAttribute('min', setMinDate());
      input.addEventListener('change', function () {
        var form = input.closest('form');
        var checkOut = form.querySelector('input[name="checkOut"]');
        if (checkOut && input.value) {
          var minCheckOut = new Date(input.value);
          minCheckOut.setDate(minCheckOut.getDate() + 1);
          var year = minCheckOut.getFullYear();
          var month = String(minCheckOut.getMonth() + 1).padStart(2, '0');
          var day = String(minCheckOut.getDate()).padStart(2, '0');
          checkOut.setAttribute('min', year + '-' + month + '-' + day);
        }
      });
    });

    document.querySelectorAll('input[name="checkOut"]').forEach(function (input) {
      var form = input.closest('form');
      var checkIn = form.querySelector('input[name="checkIn"]');
      if (checkIn && checkIn.value) {
        var minCheckOut = new Date(checkIn.value);
        minCheckOut.setDate(minCheckOut.getDate() + 1);
        var year = minCheckOut.getFullYear();
        var month = String(minCheckOut.getMonth() + 1).padStart(2, '0');
        var day = String(minCheckOut.getDate()).padStart(2, '0');
        input.setAttribute('min', year + '-' + month + '-' + day);
      }
    });
  }

  document.addEventListener('DOMContentLoaded', function () {
    bindForms('.js-register-form', validateRegisterForm);
    bindForms('.js-reservation-form', validateReservationForm);
    bindForms('.js-bill-form', validateBillingForm);
    bindForms('.js-room-form', validateRoomForm);
    bindForms('.js-roomtype-form', validateRoomTypeForm);
    initRealTimeValidation();
  });
})();