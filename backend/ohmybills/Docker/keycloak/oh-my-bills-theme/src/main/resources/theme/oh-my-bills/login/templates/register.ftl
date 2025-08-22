<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
  <#if section == "title">
    ${msg("registerTitle")}
  <#elseif section == "header">
    ${msg("registerTitle")}
  <#elseif section == "form">
    <div class="omb-card">
      <img class="omb-logo" src="${url.resourcesPath}/img/logo.svg" alt="Oh My Bills"/>
      <div class="omb-title">${msg("registerTitle")}</div>
      <div class="omb-tagline">${msg("emailUniqueInfo")}</div>

      <#if message?has_content>
        <div class="kc-feedback-text ${message.type=='error'?then('kc-feedback-error','kc-feedback-info')}">${kcSanitize(message.summary)?no_esc}</div>
      </#if>

      <form id="kc-register-form" action="${url.registrationAction}" method="post">
        <div class="omb-row">
          <input class="kc-input" type="text" id="firstName" name="firstName" value="${(register.formData.firstName!'')}"
                 placeholder="${msg('firstName')}" autocomplete="given-name" required/>
          <input class="kc-input" type="text" id="lastName" name="lastName" value="${(register.formData.lastName!'')}"
                 placeholder="${msg('lastName')}" autocomplete="family-name" required/>
        </div>

        <input class="kc-input" type="email" id="email" name="email" value="${(register.formData.email!'')}"
               placeholder="${msg('email')}" autocomplete="email" required/>

        <input class="kc-input" type="password" id="password" name="password"
               placeholder="${msg('password')}" autocomplete="new-password" required/>

        <div id="password-policy-hint" class="kc-feedback-text kc-feedback-info" style="display:none;">
          ${msg('passwordPolicyHint')}
        </div>

        <input class="kc-input" type="password" id="password-confirm" name="password-confirm"
               placeholder="${msg('passwordConfirm')}" autocomplete="new-password" required/>

        <input class="omb-btn" type="submit" value="${msg('doRegister')}"/>
      </form>

      <div style="margin-top:12px;">
        <a class="omb-link" href="${url.loginUrl}">${msg('haveAccount')} ${msg('doLogIn')}</a>
      </div>
    </div>
  <#elseif section == "info">
    <!-- seção info opcional -->
  </#if>
</@layout.registrationLayout>
