# Open Suite CRM

Desarrollo e implementación de un sistema de ofertas (CRM) basado para PYMES.

## Descripción

El proyecto contiene 3 microservicios [offers, users, notifications] donde cada uno ejecuta una funcionalidad independiente. Adicionalmente, el aplicativo cuenta con funcionalidades implementadas como JWT, Kafka, Zookeper, etc… que permiten obtener una versión funcional del código.

## Para empezar...

### Dependencies

* Para la ejecución de este aplicativo solo se requiere tener docker instalado.

### Instalación

* Descargar la última versión del software del repositorio de github.
```
git clone https://github.com/dhg-92/OpenSuite-CRM.git
```
* Acceder a la carpeta descargada
```
cd OpenSuite-CRM
```
* Ejecutar el comando:
```
docker-compose up
```

### Ejecución del software

* Una vez ejecutado el software y los contenedores levantados simplemente es necesario acceder a 
```
http://localhost:5173
```
* El puerto del aplicativo se puede cambiar previamente en el docker-compose up
  
## Autores

Nombres e información de contacto de los colaboradores

ex. Diego Higuera Goncalves

## Version

* 0.1
    * Lanzamiento inicial

## Licencia

Este proyecto está licenciado bajo la licencia de Reconocimiento-NoComercial-SinObraDerivada 3.0 España de Creative Commons

