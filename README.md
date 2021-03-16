# EazySDK - Java Client
Welcome to the **EazySDK** repository.  EazySDK is an integration of the 
[Eazy Collect API version 3](https://eazycollectservices.github.io/EazyCollectAPIv3/) built in Java 8. Its core purpose is to provide a framework for developers already working with Eazy Collect to integrate Eazy Customer Manager into their platform. The framework provides functions designed to speed up the integration process between a developer's Customer Relationship Manager and Eazy Collect. Getting started is as simple as providing user specific settings, and making your first call to Eazy Customer Manager should take less than a minute.

## Dependencies
 - JDK 1.8+
 - [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/download.html) (>= 4.5.0)
 - [Apache Commons IO](http://commons.apache.org/proper/commons-io/index.html) (>= 1.3.2)
 - [Gson](https://github.com/google/gson) (>= 2.8.0)

## Integrating EazySDK into your application

### JAR
A `.jar` file for the SDK can easily be created from this repository's files by using your preferred Java IDE.

### Maven
Add the following dependency to your Maven project:

    <dependency>
        <groupId>uk.co.eazycollect.eazysdk</groupId>
        <artifactId>eazysdk</artifactId>
        <version>1.1.0</version>
    </dependency>

***

The integration process is simple, and involves importing EazySDK into your 
 virtual environment and configuring some settings. The most basic 
configuration can be seen below.

    import uk.co.eazycollect.eazysdk.ClientHandler;
    import uk.co.eazycollect.eazysdk.Get;
       
    ClientHandler handler = new ClientHandler();
    Properties settings = handler.settings();
       
    settings.setProperty("currentEnvironment.Environment", "sandbox");
    settings.setProperty("sandboxClientDetails.ApiKey", "{Api_Key}");
    settings.setProperty("sandboxClientDetails.ClientCode", "{Client_Code}");
       
    Get get = new Get(settings);
    String response = get.customers().query();
    System.out.println(response);
       
## Documentation
All functions in EazySDK possess their own documentation, and can be viewed by viewing the `<summary>` associated with the selected function. The documentation can also be [found on GitHub](https://github.com/EazyCollectServices/EazyCollectSDK-Java/tree/master/EazySDK/docs), or in the /docs directory of the package.

## Issues
If you find any issues with EazySDK, please [raise an issue on GitHub](https://github.com/EazyCollectServices/EazyCollectSDK-Java/issues/new) detailing the issue. If this is not possible, alternatively email help@eazycollect.co.uk with as much information as you are able to provide.