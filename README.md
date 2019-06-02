## DOCKER FOR LOCAL DATABASE

1. Change directory to docker folder
  $ cd book-order-management/docker

2. Create docker machine
  $ docker-machine create --driver virtualbox book-order-machine
  
3. Attach new machine to shell
  $ eval $(docker-machine env local-machine)
  
4. Build up the database
  $ docker-compose up -d
  
5. Using sql/schema.sql to create schema 
    with 
    
    url=jdbc:mysql://192.168.99.100:3306/bkorder?useUnicode=true&characterEncoding=utf8&noAccessToProcedureBodies=true
    username=root
    password=book@dm1n