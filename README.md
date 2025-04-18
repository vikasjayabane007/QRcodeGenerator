The QR code generator is a fully serverless app.
This java app uses Zebra crossing library to generate QR code.
Frontend is a simple static web app hosted using S3 website hosting.
The front end sends the url via POST method.
The API gateaway accepts the url with the help of POST method defined in the HTTP API.
The benefit is that the API gateway easily integrates with lambda to send the url as a APIGatewayProxyRequestEvent
The lambda function extracts the url from the request and generates image in png formate using Zebra crossing library.
Then we encode the image to base64 format and return APIGatewayProxyRequestEvent with statuscode, header and base64 encoded string.
The image string is captured, and assigned to image.

Theres another easy way of lambda function url instead of using API gateway, but API gateways offers more control over API methods
and security features.

The lambda function is optimized by using snap start to reduce cold start time.
