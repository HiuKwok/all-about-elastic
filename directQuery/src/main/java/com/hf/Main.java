package com.hf;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {
    /**
     * Dummy pojo
     *
     * @param id   The id.
     * @param name The name.
     * @param date the date.
     */
    record Person(int id, String name, Date date) {
    }

    public static void main(String[] args) throws IOException {

        try (RestClient restClient = getRestClient()) {

            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);

            // Send record
            String searchText = "Mark";
            SearchResponse<Person> searchResponse = client.search(s -> s
                    .index("person")
                    .query(q -> q
                            .match(t -> t
                                    .field("name")
                                    .query(searchText))), Person.class);

            List<Hit<Person>> hits = searchResponse.hits().hits();

            // Response from server.
            System.out.println(hits.size());

            System.out.println(hits.get(0).source().name());

        }


    }

    /**
     * Establish the connection.
     *
     * @return ES connection.
     */
    private static RestClient getRestClient() {
        return RestClient
                .builder(HttpHost.create("http://localhost:9200"))
                .build();
    }
}