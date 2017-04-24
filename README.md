# Smart Foosbal IoT

Our Handsome team passionately loves foosball, mobile technology and IoT. And it was a question of time when this project will be born. Smart Foosball IoT is inspired by [Smart Foosball Handsome project](https://github.com/handsomecode/smart-foosball), but has another approach and implementation thats why it can't be named "smart foosball v2".

Article with video about old project could be found [here](http://handsome.is/smart-foosball-is-keeping-score/).

The whole project consists of three parts which depend each other:

* [Firebase](/README-firebase.md) is used as backend to collect information about matches and players.
* [Android part](/AndroidApp/README-android.md) is for routing players and goal counting. Android UI is simple and effective way to display information of all players and their statistic. Touch gestures allow to choose players conveniently for the current game.
* [Hardware part](/Arduino/README-arduino.md) is for fixing goals. This part is based on Arduino Uno-like board and has been implemented on C++.
* [Slack bot](/SlackBot/README-slackbot.md) is for looking for players and reporting results. Slack is common way of communication in our team and it's really simply to use slack bot and application for such type of purposes.

Firebase and Android are required, hardware part and slack bot are optional.
