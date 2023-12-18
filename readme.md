Project which contain all sample code that I modified throughout the journey of learning ELK stack.


Start a demo ES container by: 
 - `docker run -d --name elastic-test -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.8.2`