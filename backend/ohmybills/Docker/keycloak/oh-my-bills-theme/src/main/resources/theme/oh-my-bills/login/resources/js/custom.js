// Dicas de UX e pequenas melhorias acessíveis
(function () {
  // Mostrar requisitos de senha (apenas nas telas que têm #password)
  var pwd = document.querySelector('input[name="password"]');
  var hint = document.getElementById('password-policy-hint');
  if (pwd && hint) {
    function updateHint() {
      // só efeito visual; as regras reais são aplicadas no servidor pelo Keycloak
      hint.style.display = pwd.value.length > 0 ? 'block' : 'none';
    }
    pwd.addEventListener('input', updateHint);
    updateHint();
  }

  // Evitar duplo submit
  var forms = document.querySelectorAll('form');
  forms.forEach(function (f) {
    f.addEventListener('submit', function () {
      var btn = f.querySelector('input[type="submit"],button[type="submit"]');
      if (btn) { btn.setAttribute('disabled', 'disabled'); }
    });
  });
})();
