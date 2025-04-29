

# AddGoal


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**clubId** | **String** | Club identifier that the goal will be given. In case the given clubId is not one of the clubs playing, return 400 BAD REQUEST. For example, RMA club.id &#x3D; 1, FCB club.id&#x3D;2 and ATM club.id&#x3D;3, and it&#39;s a match against RMA vs FCB, but the provided &#x60;clubId&#x60; is 3, so it returns 400 BAD REQUEST.  |  [optional] |
|**scorerIdentifier** | **String** | Player identifier that can be a player of the opponent club, not only the club that has the player.  In case a player for the opponent club has scored,  it is called a \&quot;own goal\&quot; and so &#x60;ownGoal&#x60; attribute to be returned is to true, otherwise false.  |  [optional] |
|**minuteOfGoal** | **Integer** | Must be between 1 to 90 only, a 400 BAD REQUEST must be given if not. |  [optional] |



