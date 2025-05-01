# Running the broker client on windows

To run a fully local enrolment test you will need the following five services running and bound to different ports on localhost.

 * student-mobility-broker/client
 * student-mobility-broker/server
 * student-mobility-home-institution-mock
 * student-mobility-inteken-ontvanger-generiek
 * student-mobility-inteken-ontvanger-email (or equivalent)

## student-mobility-broker/client
Default: **http://localhost:3003**

https://github.com/SURFnet/student-mobility-broker (/client)
Start as a Node.js application using yarn.

1) Install node.js on windows
    1) From https://nodejs.org/en download and run node-v22.14.0-x64.msi
    2) When asked if you want to run a script to install additional build tools (python and some .net stuff for VS) answer yes
2) Open a new powershell terminal and navigate to where you cloned student-mobility-broker, and go into the client directory (you should be able to see package.json)
3) Run the following commands to install the dependencies (you only need to run these once) 
```
      npm update --legacy-peer-deps
      npm install --legacy-peer-deps yarn
      .\node_modules\.bin\yarn install
```
4) From now on you can start the client using the command ``.\node_modules\.bin\yarn dev``. 
   This will start the client and pop a web browser window on http://localhost:3003/.

The default test enrolment flow will start immediately - and fail unless the remaining four services are 
running and configured correctly. Restart the test by reloading http://localhost:3003/ .


## student-mobility-broker/server 
Default **http://localhost:8091**

https://github.com/SURFnet/student-mobility-broker (/server)

Start as a normal Java application.

Edit service-registry.yml if the other services are not running at their default locations.


## MyAcademicID registration
You will need to perform a MyAcademicID registration on https://tech-docs.eduxchange.eu/registration.html .
During registration set your "Redirect URLs" to where you expect your copy of student-mobility-inteken-ontvanger-generiek 
to run for external tests (f.eks. https://AIT-DWEBEUT01.win.dtu.dk/redirect_uri/).
You no not need to have a service running there yet (we will explain a workaround below), but keep in mind that the 
requests hitting this endpoint will contain your real, valid, bearer tokens for your personal university account, so 
don't set it to https://example.com/redirect_uri/ :)

**The workaround**

Once you have all your local service running, and you start the enrolment test by calling GET:http://localhost:3003 in 
a web browser (use Firefox incognito to avoid the browser side session id set by the broker client), then you will 
eventually be redirected to your institutions login page. After login, you will be redirected to <Redirect URL>. Be 
sure to have your browsers network inspect panel open and copy the full path that the browser is trying to redirect to 
(this will not show up in the address bar) - this pageload will eventually time out because no service is running at 
<Redirect URL>, this is fine.  

If you registered ``https://ait-dwebeut01.win.dtu.dk/redirect_uri/`` as your redirect, the url you copy will look like 
this:
```
https://ait-dwebeut01.win.dtu.dk/redirect_uri/?state=...&scope=openid&code=...&iss=https%3A%2F%2Fproxy.prod.erasmus.eduteams.org&client_id=...
```

Change the schema and host/port to match your local enrolment service (see below). The updated url should look like this:
```
http://localhost:8092/redirect_uri/?state=...&scope=openid&code=...&iss=https%3A%2F%2Fproxy.prod.erasmus.eduteams.org&client_id=...
```

GET this url in a new browser tab, and the rest of the enrolment flow will happen normally.


## student-mobility-home-institution-mock 
Default **http://localhost:8076**

https://github.com/SURFnet/student-mobility-home-institution-mock

Start as a normal Java application.

Update application.yml:
  * ``spring.security.oauth2.resourceserver.opaque-token.introspection-uri = https://proxy.prod.erasmus.eduteams.org/OIDC/introspect``
  * ``spring.security.oauth2.resourceserver.opaque-token.client-id = <from MyAcademicID registration>``
  * ``spring.security.oauth2.resourceserver.opaque-token.client-secret = <from MyAcademicID registration>``


## student-mobility-inteken-ontvanger-generiek 
Default **http://localhost:8092**

https://github.com/SURFnet/student-mobility-inteken-ontvanger-generiek

Start as a normal Java application.

You may need to make the following edits to application.yml

If you change the jpa settings to use postgres then you will need to add ([stackoverflow](https://stackoverflow.com/questions/49110818/method-org-postgresql-jdbc-pgconnection-createclob-is-not-yet-implemented#49261146)): 
  * ``spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation: true``  

If you are not a Dutch university set:
  * ``features.require_eduid: false``

Set ``sis.user`` and ``sis.password`` to be the basic auth credentials that the enrolment service will use when calling 
this service to request an association request me sent to the home university.

Set ``backend.url``, ``backend.api_user``, and ``backend.api_password`` to be service url and basic auth credentials 
that this service will call when it requests that the enrolment service handle the enrolment. An example of a service 
url is http://localhost:8083/intake .

To make the OAuth MyAcademicID work set:
  * ``oidc.client-id: <from MyAcademicID registration>``
  * ``oidc.client-secret: <from MyAcademicID registration>``
  * ``oidc.redirect-uri: <from MyAcademicID registration>``
  * ``oidc.authorization-uri: https://proxy.prod.erasmus.eduteams.org/OIDC/authorization``
  * ``oidc.token-uri: https://proxy.prod.erasmus.eduteams.org/OIDC/token``
  * ``oidc.jwk-set-uri: https://proxy.prod.erasmus.eduteams.org/OIDC/jwks``


## your custom enrolment service
Default **http://localhost:8093**

This is where you need to write your own service that connects to your local SIS in a custom way.
A reference implementation is provided in [student-mobility-inteken-ontvanger-email](https://github.com/SURFnet/student-mobility-inteken-ontvanger-email) 

You will need to make a single endpoint called POST:/intake that accepts a JSON body: 
```
{
  "person" : <https://openonderwijsapi.nl/specification/v5/docs.html#tag/person_model>
  "offering" : <https://openonderwijsapi.nl/specification/v5/docs.html#tag/program_offering_model>
}
```

... and is authorized using the basic auth credentials shared with student-mobility-inteken-ontvanger-generiek above.

Besides deciding if the host university accepts the students enrolment request, this service must also communicate this 
decision back to home university. Do this by calling the appropriate existing endpoints on your local copy of  
student-mobility-inteken-ontvanger-generiek using the second set of basic auth credentials that we set in the previous 
section. 