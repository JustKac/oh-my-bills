<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
  <#if section == "title">
    ${msg("resetTitle")}
  <#elseif section == "header">
    ${msg("resetTitle")}
  <#elseif section == "form">
    <div class="omb-card">
      <img class="omb-logo" src="${url.resourcesPath}/img/logo.svg" alt="Oh My Bills"/>
      <div class="omb-title">${msg("resetTitle")}</div>
      <div class="omb-tagline">${msg("resetByEmailInfo")}</div>

      <#if message?has_content>
        <div class="kc-feedback-text ${message.type=='error'?then('kc-feedback-error','kc-feedback-info')}">${kcSanitize(message.summary)?no_esc}</div>
      </#if>

      <form id="kc-reset-password-form" action="${url.loginAction}" method="post">
        <input class="kc-input" type="text" id="username" name="username"
               value="${(login.username!'')}" placeholder="${msg('email')}" autocomplete="email" required/>
        <input class="omb-btn" type="submit" value="${msg('doSubmit')}"/>
      </form>

      <div style="margin-top:12px;">
        <a class="omb-link" href="${url.loginUrl}">${msg('doBackToLogin')}</a>
      </div>

      <div class="omb-footer">
        O link de recuperação expira em 30 minutos.
      </div>
    </div>
  <#elseif section == "info">
  </#if>
</@layout.registrationLayout>
