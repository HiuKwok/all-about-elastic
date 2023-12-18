package com.hf;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Date;
import java.util.random.RandomGenerator;

/**
 * Ref: https://www.baeldung.com/elasticsearch-java
 *
 * Start a demo ES container by: docker run -d --name elastic-test -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.8.2
 *
 * Demo app to insert a record directly to Elasticsearch, which is not recommended thou, should be done over MQ with LogStash.
 */
public class Main {

    /**
     * Dummy pojo
     * @param id The id.
     * @param name The name.
     * @param date the date.
     */
    record Person (int id, String name, Date date){}

    public static void main(String[] args) throws IOException {

        try (RestClient restClient = getRestClient()) {

            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);

            // Prep record
            Person person = new Person(RandomGenerator.getDefault().nextInt(), "Mark Doe", new Date(1471466076564L));
            // Send record
            IndexResponse response = client.index(i -> i
                    .index("person")
                    .id(person.name())
                    .document(person));

            // Response from server.
            System.out.println(response);

        }



    }

    /**
     * Establish the connection.
     * @return ES connection.
     */
    private static RestClient getRestClient() {
        return RestClient
                .builder(HttpHost.create("http://localhost:9200"))
                .build();
    }
}