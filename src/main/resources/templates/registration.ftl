<#-- @ftlvariable name="userSurname" type="java.lang.String" -->
<#-- @ftlvariable name="userName" type="java.lang.String" -->
<#-- @ftlvariable name="userEmail" type="java.lang.String" -->
<#import "ui.ftl" as ui/>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
</head>

<body>
<div>
    <form name="user" action="save" method="POST">
        <@ui.formInput id="t1" name="username" label="Username"/> <br/>
        <@ui.formInput id="t2" name="password" label="Password"/> <br/>
        <@ui.formInput id="t3" name="email" label="Email" value="${userEmail}"/> <br/>
        <@ui.formInput id="t4" name="name" label="First name" value="${userName}"/> <br/>
        <@ui.formInput id="t5" name="surname" label="Last name" value="${userSurname}"/> <br/>
        <input type="submit" value="Save"/>
    </form>
</div>

</body>
</html>