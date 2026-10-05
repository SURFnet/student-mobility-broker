# Running the broker client on windows

Start as a Node.js application using yarn.

1) Install node.js on windows
    1) From https://nodejs.org/en download and run node-v22.14.0-x64.msi
    2) When asked if you want to run a script to install additional build tools (python and some .net stuff for VS) you can answer No - this project does not need it
2) Open a new powershell terminal and navigate to where you cloned student-mobility-broker, and go into the client directory (you should be able to see package.json)
3) Run the following commands to install the dependencies (you only need to run these once) 
```
      npm update --legacy-peer-deps
      npm install --legacy-peer-deps yarn
      .\node_modules\.bin\yarn install
```
4) From now on you can start the client from a windows terminal or a powershell terminal using the command ``.\node_modules\.bin\yarn dev`` (you need to be in the dir where you installed yarn). This will start the client and pop a web browser window on http://localhost:3003/.

The default test enrolment flow will start immediately - and fail unless the remaining four services are 
running and configured correctly. Restart the test by reloading http://localhost:3003/ .
