# Coyote Forms Examples

You will need `JDK 11`, `maven` and `npm` to build and run the examples.

To build the backend, execute from the project root

`mvn clean install`

This will build the lib and the example backend modules.

To start the backend, execute from the project root:

`java -jar examples\coyote-forms-example-backend\target\coyote-forms-example-0.1-SNAPSHOT.jar`

The backend will listen onn port 8080.

To build the frontend, execute from the project root:
```
cd examples\coyote-forms-example-frontend
npm install
```
This will download necessary libraries and create a development build. To start the front-end, execute from the current folder:

`npm start`

The front-end will listen on port 3000, open it at [http://localhost:3000](http://localhost:3000) .
