# EAM WSHub
EAM WSHub is Web application that provides a simplified REST/SOAP facade to Infor EAM. 
EAM WSHub is available as a Docker image here: https://hub.docker.com/r/cerneam/eam-wshub

## Configuration
The docker image needs to be parametrized with environment variables.



| Variable        | Required?  | Default value |
| ------------- | -----:|---------:|
| WSHUB_INFOR_WS_URL           | **Yes** |  |
| WSHUB_INFOR_TENANT         | No |  |
| WSHUB_INFOR_ORGANIZATION         | No |  |


Please not that for the moment the database connection is not supported and the non-mandatory parameters above won't be considered.

You can for instance store your environment variables in a dedicated .env file:

```
WSHUB_INFOR_WS_URL=<url>
WSHUB_INFOR_TENANT=<tenant>
WSHUB_INFOR_ORGANIZATION=<organization>
```


## Run

The docker container exposes the following ports:

| Port        | Description  |
| ------------- | -----:|
| 8080          | EAM WSHub | 
| 9090          | JBoss Management Port |

Once you have your own environment variables set up, you can start a new docker container:
```
docker run -p 8080:8080 -p 9090:9090 --env-file .env cerneam/eam-wshub:latest
``` 

Once the docker container is started, the WSDLs is accessible on port 8080 at the endpoint `/SOAPWS/SOAP?wsdl`
The REST web services are documented with Swagger (`/RESTWS/REST/docs`)

## License
This software is published under the GNU General Public License v3.0 or later.