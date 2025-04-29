# SeasonApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createSeasons**](SeasonApi.md#createSeasons) | **POST** /seasons | Create list of new seasons |
| [**getSeasons**](SeasonApi.md#getSeasons) | **GET** /seasons | Get list of existing seasons |
| [**updateSeasonStatus**](SeasonApi.md#updateSeasonStatus) | **PUT** /seasons/{seasonYear}/status | Update a specific season status |


<a id="createSeasons"></a>
# **createSeasons**
> List&lt;Season&gt; createSeasons(createSeason)

Create list of new seasons

Required request body without ID or status, default computed status is NOT_STARTED. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SeasonApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    SeasonApi apiInstance = new SeasonApi(defaultClient);
    List<CreateSeason> createSeason = Arrays.asList(); // List<CreateSeason> | 
    try {
      List<Season> result = apiInstance.createSeasons(createSeason);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SeasonApi#createSeasons");
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
| **createSeason** | [**List&lt;CreateSeason&gt;**](CreateSeason.md)|  | [optional] |

### Return type

[**List&lt;Season&gt;**](Season.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of all seasons including new seasons created |  -  |

<a id="getSeasons"></a>
# **getSeasons**
> Season getSeasons()

Get list of existing seasons

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SeasonApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    SeasonApi apiInstance = new SeasonApi(defaultClient);
    try {
      Season result = apiInstance.getSeasons();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SeasonApi#getSeasons");
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

[**Season**](Season.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of seasons |  -  |

<a id="updateSeasonStatus"></a>
# **updateSeasonStatus**
> Season updateSeasonStatus(seasonYear, updateSeasonStatus)

Update a specific season status

Following order can be accepted : NOT_STARTED &gt; STARTED &gt; FINISHED. In case you change status update order, a 400 BAD_REQUEST response must be return. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SeasonApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    SeasonApi apiInstance = new SeasonApi(defaultClient);
    LocalDate seasonYear = LocalDate.now(); // LocalDate | 
    UpdateSeasonStatus updateSeasonStatus = new UpdateSeasonStatus(); // UpdateSeasonStatus | 
    try {
      Season result = apiInstance.updateSeasonStatus(seasonYear, updateSeasonStatus);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SeasonApi#updateSeasonStatus");
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
| **updateSeasonStatus** | [**UpdateSeasonStatus**](UpdateSeasonStatus.md)|  | [optional] |

### Return type

[**Season**](Season.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Season with updated status |  -  |

