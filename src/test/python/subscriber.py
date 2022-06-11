from gql import gql, Client
from gql.transport.websockets import WebsocketsTransport

if __name__ == '__main__':
    transport = WebsocketsTransport(
        url="ws://localhost:8080/graphql",
        subprotocols=[WebsocketsTransport.GRAPHQLWS_SUBPROTOCOL]
    )

    client = Client(transport=transport, fetch_schema_from_transport=True)

    query = gql(
        '''
        subscription personCreated{
        personCreated {
            id
            name
        }
        }
        '''
    )

    for result in client.subscribe(query):
        print(result)
