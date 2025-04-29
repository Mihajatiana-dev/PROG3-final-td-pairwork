# ClubApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**clubsIdPlayersGet**](ClubApi.md#clubsIdPlayersGet) | **GET** /clubs/{id}/players | Get actual players of the specific club |
| [**clubsIdPlayersPost**](ClubApi.md#clubsIdPlayersPost) | **POST** /clubs/{id}/players | Add new players or existing players to the specific club |
| [**clubsIdPlayersPut**](ClubApi.md#clubsIdPlayersPut) | **PUT** /clubs/{id}/players | Change players of the specific club |
| [**clubsStatisticsSeasonYearGet**](ClubApi.md#clubsStatisticsSeasonYearGet) | **GET** /clubs/statistics/{seasonYear} | Get all clubs statistics for a specific season year |
| [**createOrUpdateClubs**](ClubApi.md#createOrUpdateClubs) | **PUT** /clubs | Create new clubs or update if already exist |
| [**getClubs**](ClubApi.md#getClubs) | **GET** /clubs | Get clubs of the championship |


<a id="clubsIdPlayersGet"></a>
# **clubsIdPlayersGet**
> List&lt;Player&gt; clubsIdPlayersGet(id)

Get actual players of the specific club

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    String id = "id_example"; // String | 
    try {
      List<Player> result = apiInstance.clubsIdPlayersGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#clubsIdPlayersGet");
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

### Return type

[**List&lt;Player&gt;**](Player.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of players of the club |  -  |
| **404** |  |  -  |

<a id="clubsIdPlayersPost"></a>
# **clubsIdPlayersPost**
> List&lt;Player&gt; clubsIdPlayersPost(id, player)

Add new players or existing players to the specific club

Provided players inside the requestBody create the players if do not exist,  or attached them to the club, if the players are not attached to a club. In case, one of existing players is still attached to a club, API must return 400 BAD_REQUEST. Also, in case players exist and identified by its ID, player provided values do not update existing player attributes. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    String id = "id_example"; // String | 
    List<Player> player = Arrays.asList(); // List<Player> | 
    try {
      List<Player> result = apiInstance.clubsIdPlayersPost(id, player);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#clubsIdPlayersPost");
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
| **200** | List of players of the specific club, including old players and new ones |  -  |
| **400** | Bad Request when players still attached to another club |  -  |
| **404** |  |  -  |

<a id="clubsIdPlayersPut"></a>
# **clubsIdPlayersPut**
> List&lt;Player&gt; clubsIdPlayersPut(id, player)

Change players of the specific club

Provided players inside the requestBody erase the existing players inside the club. In case player is detached from club, it is still possible to retrieve his individual statistics  and collective statistics do not change.  For example, the player has 10 goals for the season, even if he is not part of the club anymore,  the club statistics do not change (goals scored). Finally, he must not be inside the list of players can make actions anymore for the remaining matches, if the season is not yet finished.         In case, one of existing players is still attached to a club, API must return 400 BAD_REQUEST. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    String id = "id_example"; // String | 
    List<Player> player = Arrays.asList(); // List<Player> | 
    try {
      List<Player> result = apiInstance.clubsIdPlayersPut(id, player);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#clubsIdPlayersPut");
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
| **200** | List of actual players of the specific club |  -  |
| **400** | Bad Request when players still attached to another club |  -  |
| **404** |  |  -  |

<a id="clubsStatisticsSeasonYearGet"></a>
# **clubsStatisticsSeasonYearGet**
> List&lt;ClubStatistics&gt; clubsStatisticsSeasonYearGet(seasonYear, hasToBeClassified)

Get all clubs statistics for a specific season year

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    LocalDate seasonYear = LocalDate.now(); // LocalDate | 
    Boolean hasToBeClassified = true; // Boolean | Default value is false, and if so, return list of statistics ordered by club name ASC. If provided value is true, return list of statistics ordered by rankings. Ranking is computed respectively according to the following orders of factors: 1. Ranking points : club with most ranking points goes on top 2. Difference goals : if some clubs have same ranking points, club with most difference goals goes on top 3. Clean sheets number : if some clubs have same both ranking points and difference goals, club with most clean sheets goes on top 
    try {
      List<ClubStatistics> result = apiInstance.clubsStatisticsSeasonYearGet(seasonYear, hasToBeClassified);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#clubsStatisticsSeasonYearGet");
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
| **seasonYear** | **LocalDate**|  | |
| **hasToBeClassified** | **Boolean**| Default value is false, and if so, return list of statistics ordered by club name ASC. If provided value is true, return list of statistics ordered by rankings. Ranking is computed respectively according to the following orders of factors: 1. Ranking points : club with most ranking points goes on top 2. Difference goals : if some clubs have same ranking points, club with most difference goals goes on top 3. Clean sheets number : if some clubs have same both ranking points and difference goals, club with most clean sheets goes on top  | [optional] |

### Return type

[**List&lt;ClubStatistics&gt;**](ClubStatistics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | All club statistics for the specific season |  -  |

<a id="createOrUpdateClubs"></a>
# **createOrUpdateClubs**
> List&lt;Club&gt; createOrUpdateClubs(club)

Create new clubs or update if already exist

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    List<Club> club = Arrays.asList(); // List<Club> | 
    try {
      List<Club> result = apiInstance.createOrUpdateClubs(club);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#createOrUpdateClubs");
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
| **club** | [**List&lt;Club&gt;**](Club.md)|  | [optional] |

### Return type

[**List&lt;Club&gt;**](Club.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of created or updated clubs |  -  |

<a id="getClubs"></a>
# **getClubs**
> List&lt;Club&gt; getClubs()

Get clubs of the championship

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ClubApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    ClubApi apiInstance = new ClubApi(defaultClient);
    try {
      List<Club> result = apiInstance.getClubs();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ClubApi#getClubs");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Club&gt;**](Club.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of the clubs |  -  |

