# Currency Conversion App


## How to use the app

After installed, the application will load the data at the startup. Press into a currency box to select it as the main conversion currency and insert a value in the edit text. As the user inserts a value, all the rates' total value are going to be updated, having the selected one as the base rate used for the conversion.

### Current Solution

During the development of the application, the use of [Currency Layer API](https://currencylayer.com/documentation) free subscription presented as a challenge for the the business rules. While we should be able to convert the rates based on every valid option from the API list, the current subscription is able to return the rates only for US Dollar. 

To get the rates for the other currencies, it was needed to make a calculation, based on the dollar rates. So, based on both the dollar and the current base rate, it is calculated the proportion between them and multiplying it by the current value. It can cause minor difference between the real value and the app's converted one, but understanding that floating-point error is acceptable, the this difference falls into this range.

### Caching And Internet

Based on the requirements, it was understood that the rates should be persisted locally to limit band usage, but it doesn't mean the app should work without internet. So, both the call for the available exchanges and exchange rates have no fallback for no internet available, throwing an error on the screen in the form of a snackbar including a retry button. 

Also, please note that was needed to add a delay between the currency list and currency ratios call. Since we got to use the free plan for the API, it returns error if they are two calls too close to the each other.

For the exchange rates, it is being cached on the system using [Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=EAIaIQobChMIxf3064KQ8wIVwYORCh3bYgC8EAAYASAAEgL25fD_BwE&gclsrc=aw.ds). Based on it's current timestamp it checks if is needed to reload the cached data, retrieving from API or use the current one in cache.


## Architecture

The project follows the MVVM Architecture as endorsed by Google. Based on the clean architecture, it had bottom to top development, going from the entities that implements the business rules to the activies and views.
The development of the entities were done using TDD, what was able to generate tests for methods regarding mostly the data source, it's logics and implementation and also the conversion of the repository classes to the ones used by the application.

The integrations between the modules and it's objects was developed using the dependency injection pattern. The immutability of the objects of different modules and layers are secured by the Interface Segregation Principle, asserting that the only communication between then is trough an interface. Below, I've added a simple description of the architecture developed for the project. Below we can check a basic sketch based on the main elements for the architecture developed for the application.

<img width="1071" alt="Basic Architecture Structure" src="https://user-images.githubusercontent.com/11355552/134177383-50958259-3ff6-4143-9796-c0b641da30d1.png">

## Libraries and Technologies

### Retrofit
It is my tested and true REST library. I don't think there is much talk needed about that, based on it's popularity. Is my library of choice for api calls mostly because it is the one I am most confortable with and it's integration with rxJava/rxAndroid and GSON.

### RxJava
This extremely powerful library has a great integration with Retrofit and serves me the purpose of making the development easier because of it's functional programming characteristics.

### Room
I believe that there are more powerful persistence databases, like REALM, for example, but I didn't see the need of that in the project application. It's easy development and lightweight characteristics was enough to justify the use of Room as the go to tool for simple data persistence and caching.

### Koin
A dependency injection that is a lot simpler than DAGGER. While being lightweight, it also conforms better with Kotlin.

