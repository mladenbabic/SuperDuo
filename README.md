# Super Duo
    
## Projects:

   * Alexandria
   * Football Scores
   
## Instalation
   
   The project Football Scores retrieves the match soccer data from [Football Data](http://api.football-data.org/index). The app requires your API key from http://api.football-data.org to work properly. When you obtain API key, replace API_KEY with your API key in the strings.xml file.
         
          buildTypes.each {
               it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', '"YOUR_API_KEY"'
          }


## Third-Party Libs

* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Glide](https://github.com/bumptech/glide)
* [Retrofit](https://github.com/square/retrofit)
* [Gson](https://github.com/google/gson)
* [EventBus](https://github.com/greenrobot/EventBus)
* [Stetho](http://facebook.github.io/stetho)
* [MaterialViewPager](https://github.com/florent37/MaterialViewPager)

## Android Developer Nanodegree by Udacity

[Nanodegree Course] https://www.udacity.com/course/android-developer-nanodegree--nd801

## License

    Copyright 2015 Babic Mladen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.