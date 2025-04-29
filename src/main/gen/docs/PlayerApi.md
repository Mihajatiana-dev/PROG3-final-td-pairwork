# PlayerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createOrUpdatePlayers**](PlayerApi.md#createOrUpdatePlayers) | **PUT** /players | Create or update players without attaching them into club |
| [**getPlayers**](PlayerApi.md#getPlayers) | **GET** /players | Get list of players in the championship |
| [**getStatisticsOfPlayerById**](PlayerApi.md#getStatisticsOfPlayerById) | **GET** /players/{id}/statistics/{seasonYear} | Get statistics for a specific player |


<a id="createOrUpdatePlayers"></a>
# **createOrUpdatePlayers**
> List&lt;Player&gt; createOrUpdatePlayers(player)

Create or update players without attaching them into club

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PlayerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    PlayerApi apiInstance = new PlayerApi(defaultClient);
    List<Player> player = Arrays.asList(); // List<Player> | 
    try {
      List<Player> result = apiInstance.createOrUpdatePlayers(player);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PlayerApi#createOrUpdatePlayers");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **player** | [**List&lt;Player&gt;**](Player.md)|  | [optional] |

### Return type

[**List&lt;Player&gt;**](Player.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of created or updated players |  -  |

<a id="getPlayers"></a>
# **getPlayers**
> List&lt;ClubPlayer&gt; getPlayers(name, ageMinimum, ageMaximum, clubName)

Get list of players in the championship

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PlayerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    PlayerApi apiInstance = new PlayerApi(defaultClient);
    String name = "name_example"; // String | 
    Integer ageMinimum = 56; // Integer | 
    Integer ageMaximum = 56; // Integer | 
    String clubName = "clubName_example"; // String | 
    try {
      List<ClubPlayer> result = apiInstance.getPlayers(name, ageMinimum, ageMaximum, clubName);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PlayerApi#getPlayers");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **name** | **String**|  | [optional] |
| **ageMinimum** | **Integer**|  | [optional] |
| **ageMaximum** | **Integer**|  | [optional] |
| **clubName** | **String**|  | [optional] |

### Return type

[**List&lt;ClubPlayer&gt;**](ClubPlayer.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of players with their clubs |  -  |

<a id="getStatisticsOfPlayerById"></a>
# **getStatisticsOfPlayerById**
> PlayerStatistics getStatisticsOfPlayerById(id, seasonYear)

Get statistics for a specific player

Important ! Note that own goals are not considered as goal inside goals scored. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PlayerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    PlayerApi apiInstance = new PlayerApi(defaultClient);
    String id = "id_example"; // String | 
    LocalDate seasonYear = LocalDate.now(); // LocalDate | 
    try {
      PlayerStatistics result = apiInstance.getStatisticsOfPlayerById(id, seasonYear);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PlayerApi#getStatisticsOfPlayerById");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | |
| **seasonYear** | **LocalDate**|  | |

### Return type

[**PlayerStatistics**](PlayerStatistics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Player statistics |  -  |
| **404** |  |  -  |

