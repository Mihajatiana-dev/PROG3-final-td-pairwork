# MatchApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**matchMakerSeasonYearPost**](MatchApi.md#matchMakerSeasonYearPost) | **POST** /matchMaker/{seasonYear} | Create all matches for a specific season including all clubs |
| [**matchesIdGoalsPost**](MatchApi.md#matchesIdGoalsPost) | **POST** /matches/{id}/goals | Add goals by player to a specific match |
| [**matchesIdStatusPut**](MatchApi.md#matchesIdStatusPut) | **PUT** /matches/{id}/status | Change a specific match status |
| [**matchesSeasonYearGet**](MatchApi.md#matchesSeasonYearGet) | **GET** /matches/{seasonYear} | Get all matches for a specific season |


<a id="matchMakerSeasonYearPost"></a>
# **matchMakerSeasonYearPost**
> List&lt;Match&gt; matchMakerSeasonYearPost(seasonYear)

Create all matches for a specific season including all clubs

Only season with status STARTED can compute matches, in case season status is either NOT_STARTED or FINISHED, must return 400 BAD_REQUEST. In case provided season matches already generated, then must return 400 BAD_REQUEST response. Otherwise, create two matches for each club against other, one &#39;HOME&#39; match and one &#39;AWAY&#39; match. Look at the instructions inside the context to have more explanation and examples. Also, in case the provided &#x60;seasonYear&#x60; does not exist, returns 404 NOT_FOUND response. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MatchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    MatchApi apiInstance = new MatchApi(defaultClient);
    LocalDate seasonYear = LocalDate.now(); // LocalDate | 
    try {
      List<Match> result = apiInstance.matchMakerSeasonYearPost(seasonYear);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MatchApi#matchMakerSeasonYearPost");
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

### Return type

[**List&lt;Match&gt;**](Match.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of created matches for the season |  -  |

<a id="matchesIdGoalsPost"></a>
# **matchesIdGoalsPost**
> Match matchesIdGoalsPost(id, addGoal)

Add goals by player to a specific match

Only match with status STARTED can be added goals. If NOT_STARTED or FINISHED status, must return 400 BAD_REQUEST. After each request, the scorer player statistics must be updated, that means, after a goal is scored, response return by GET /players/{id}/statistics/{seasonYear} should be updated. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MatchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    MatchApi apiInstance = new MatchApi(defaultClient);
    String id = "id_example"; // String | 
    List<AddGoal> addGoal = Arrays.asList(); // List<AddGoal> | 
    try {
      Match result = apiInstance.matchesIdGoalsPost(id, addGoal);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MatchApi#matchesIdGoalsPost");
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
| **addGoal** | [**List&lt;AddGoal&gt;**](AddGoal.md)|  | [optional] |

### Return type

[**Match**](Match.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Match with updated goals |  -  |

<a id="matchesIdStatusPut"></a>
# **matchesIdStatusPut**
> Match matchesIdStatusPut(id, updateMatchStatus)

Change a specific match status

Following order is to be accepted : NOT_STARTED &gt; STARTED &gt; FINISHED. In case you change status order, a 400 BAD_REQUEST response must be return. When match is set to status FINISHED, club rankings and statistics must be updated,  that means after a match is finished, response return by GET /clubs/statistics/{seasonYear} should be updated. For reminder, this is the rules about points obtained after each match according to its result :  1. In case of victory, the winner obtains 3 points. 2. In cas of draw, each club of the match obtains 1 point. 3. In case of defeat, the loser obtains 0 point. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MatchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    MatchApi apiInstance = new MatchApi(defaultClient);
    String id = "id_example"; // String | 
    UpdateMatchStatus updateMatchStatus = new UpdateMatchStatus(); // UpdateMatchStatus | 
    try {
      Match result = apiInstance.matchesIdStatusPut(id, updateMatchStatus);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MatchApi#matchesIdStatusPut");
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
| **updateMatchStatus** | [**UpdateMatchStatus**](UpdateMatchStatus.md)|  | [optional] |

### Return type

[**Match**](Match.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Match with updated status |  -  |

<a id="matchesSeasonYearGet"></a>
# **matchesSeasonYearGet**
> List&lt;Match&gt; matchesSeasonYearGet(seasonYear, matchStatus, clubPlayingName, matchAfter, matchBeforeOrEquals)

Get all matches for a specific season

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MatchApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    MatchApi apiInstance = new MatchApi(defaultClient);
    LocalDate seasonYear = LocalDate.now(); // LocalDate | 
    MatchStatus matchStatus = MatchStatus.fromValue("NOT_STARTED"); // MatchStatus | Return list filtered by provided matchStatus value.  For example, if provided matchStatus = FINISHED, then only FINISHED matches will be returned. 
    String clubPlayingName = "clubPlayingName_example"; // String | Return list filtered by provided clubPlayingName value containing. For example, if provided clubPlayingName = 'Real', then return all matches that contains 'Real' as clubs playing name,  both if the clubs play HOME or AWAY. 
    LocalDate matchAfter = LocalDate.now(); // LocalDate | Return matches after the provided date only. In case both `matchAfter` and `matchBeforeOrEquals` parameters are provided,  Return list of matches between date intervals. 
    LocalDate matchBeforeOrEquals = LocalDate.now(); // LocalDate | Return matches before or equals the provided date only. In case both `matchAfter` and `matchBeforeOrEquals` parameters are provided,  Return list of matches between date intervals. 
    try {
      List<Match> result = apiInstance.matchesSeasonYearGet(seasonYear, matchStatus, clubPlayingName, matchAfter, matchBeforeOrEquals);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MatchApi#matchesSeasonYearGet");
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
| **matchStatus** | [**MatchStatus**](.md)| Return list filtered by provided matchStatus value.  For example, if provided matchStatus &#x3D; FINISHED, then only FINISHED matches will be returned.  | [optional] [enum: NOT_STARTED, STARTED, FINISHED] |
| **clubPlayingName** | **String**| Return list filtered by provided clubPlayingName value containing. For example, if provided clubPlayingName &#x3D; &#39;Real&#39;, then return all matches that contains &#39;Real&#39; as clubs playing name,  both if the clubs play HOME or AWAY.  | [optional] |
| **matchAfter** | **LocalDate**| Return matches after the provided date only. In case both &#x60;matchAfter&#x60; and &#x60;matchBeforeOrEquals&#x60; parameters are provided,  Return list of matches between date intervals.  | [optional] |
| **matchBeforeOrEquals** | **LocalDate**| Return matches before or equals the provided date only. In case both &#x60;matchAfter&#x60; and &#x60;matchBeforeOrEquals&#x60; parameters are provided,  Return list of matches between date intervals.  | [optional] |

### Return type

[**List&lt;Match&gt;**](Match.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of matches of the specific season |  -  |
| **404** |  |  -  |

