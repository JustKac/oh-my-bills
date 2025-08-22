<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
  <#if section == "title">
    ${msg("loginTitle")}
  <#elseif section == "header">
    ${msg("ohmybills.welcome")}
  <#elseif section == "form">
    <div class="omb-card">
      <img class="omb-logo" src="${url.resourcesPath}/img/logo.svg" alt="Oh My Bills"/>
      <div class="omb-title">${msg("ohmybills.welcome")}</div>
      <div class="omb-tagline">${msg("ohmybills.tagline")}</div>

      <#if message?has_content>
        <div class="kc-feedback-text ${message.type=='error'?then('kc-feedback-error','kc-feedback-info')}">${kcSanitize(message.summary)?no_esc}</div>
      </#if>

      <form id="kc-form-login" action="${url.loginAction}" method="post">
        <input class="kc-input" type="text" id="username" name="username"
               value="${(login.username!'')}" placeholder="${msg('usernameOrEmail')}" autocomplete="username" required/>

        <input class="kc-input" type="password" id="password" name="password"
               placeholder="${msg('password')}" autocomplete="current-password" required/>

        <input class="omb-btn" name="login" id="kc-login" type="submit" value="${msg('doLogIn')}"/>
      </form>

      <div style="margin-top:12px; display:flex; justify-content:space-between;">
        <#if realm.resetPasswordAllowed>
          <a class="omb-link" href="${url.loginResetCredentialsUrl}">${msg('doForgotPassword')}</a>
        </#if>
        <#if realm.registrationAllowed>
          <a class="omb-link" href="${url.registrationUrl}">${msg('doRegister')}</a>
        </#if>
      </div>

      <div class="omb-footer">
        Tokens são validados a cada requisição via API após a autenticação.
      </div>
    </div>
  <#elseif section == "info">
    <!-- seção info opcional -->
  </#if>
</@layout.registrationLayout>
