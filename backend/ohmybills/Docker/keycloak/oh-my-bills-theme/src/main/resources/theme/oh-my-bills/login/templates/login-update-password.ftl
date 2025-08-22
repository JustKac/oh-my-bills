<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
  <#if section == "title">
    ${msg("updatePasswordTitle")}
  <#elseif section == "header">
    ${msg("updatePasswordTitle")}
  <#elseif section == "form">
    <div class="omb-card">
      <img class="omb-logo" src="${url.resourcesPath}/img/logo.svg" alt="Oh My Bills"/>
      <div class="omb-title">${msg("updatePasswordTitle")}</div>
      <div class="omb-tagline">${msg("passwordPolicyHint")}</div>

      <#if message?has_content>
        <div class="kc-feedback-text ${message.type=='error'?then('kc-feedback-error','kc-feedback-info')}">${kcSanitize(message.summary)?no_esc}</div>
      </#if>

      <form id="kc-passwd-update-form" action="${url.loginAction}" method="post">
        <input class="kc-input" type="password" id="password-new" name="password-new"
               placeholder="${msg('password')}" autocomplete="new-password" required/>

        <div id="password-policy-hint" class="kc-feedback-text kc-feedback-info" style="display:none;">
          ${msg('passwordPolicyHint')}
        </div>

        <input class="kc-input" type="password" id="password-confirm" name="password-confirm"
               placeholder="${msg('passwordConfirm')}" autocomplete="new-password" required/>

        <input class="omb-btn" type="submit" value="${msg('doUpdate')}"/>
      </form>

      <div style="margin-top:12px;">
        <a class="omb-link" href="${url.loginUrl}">${msg('doBackToLogin')}</a>
      </div>
    </div>
  <#elseif section == "info">
  </#if>
</@layout.registrationLayout>
