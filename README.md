# Interaction Cards Layout

[![Join the chat at https://gitter.im/panterdialog/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/panterdialog/Lobby)

![Logo](http://i.imgur.com/GL1AM)

## Installation
Add this into your build.gradle dependencies section.
```
compile 'com.eminayar.interactioncards:cardhelper:0.0.0.1'
```

## Sample Usages

You can check sample application to see how to user Interaction Cards on your layout. It is 
quite simply to use card layout in your any kind of layout. Just add it to front index of 
your layout.
 
### XML Part of Usage
 
 ````xml
 <?xml version="1.0" encoding="utf-8"?>
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:background="@android:color/white"
     >
 
   <eminayar.com.cardhelper.HelperCardsLayout
       android:id="@+id/cardHelper"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:back_icon="@drawable/helper_back"
       app:overlay_color="#000000"
       app:toolbar_title="Messages"
       />
 
 </RelativeLayout>
 ````
 
You can find which attributes you can use on xml below

| Attribute | Usage | Default Value |
| --- | --- |--- |
| app:back_icon | @drawable/your_back_icon | pre defined back icon |
| app:overlay_color | @drawable/your_back_icon | #000000 |
| app:toolbar_title | Any string you can define here | Messages |

Interaction Cards Layout with simply [sample module] (https://github.com/kngfrhzs/Interaction-Cards-Layout/blob/master/app/src/main/java/eminayar/com/helpercards/MainActivity.java).

There is ```Card Item``` model defined in project. You should create an `ArrayList<CardItem>` to 
inflate your cards. It is basic model that contains ; `title, description, image`. 

### JAVA Part of Usage

````java
HelperCardsLayout layout = (HelperCardsLayout) findViewById(R.id.cardHelper);

    ArrayList<CardItem> cardItems = new ArrayList<>();

    cardItems.add(new CardItem("Short Title With Image",
        "Description this can be some long text, " + "layout" + "will scale itself",
        R.drawable.barchart));

    cardItems.add(new CardItem("Short Title With No Image",
        "Description this can be some long text, layout" + "will scale itself"));

    cardItems.add(new CardItem("Long title example to demonstrate users how can this textview can"
        + " be longer and longer with image",
        "Description this can be some long text, layout" + "will scale itself",
        R.drawable.getmoney));

    cardItems.add(new CardItem("Long title example to demonstrate users how can this textview can"
        + " be longer and longer without image",
        "Description this can be some long text, layout" + "will scale itself"));

    cardItems.add(new CardItem("Very very long description example",
        "Description this can be some long text, layout"
            + "will scale itself"
            + "Description this can be some long text, layout"
            + "will scale itself"
            + "Description this can be some long text, layout"
            + "will scale itself", R.drawable.celebration));

    layout.setItems(cardItems);
    layout.setOnCardClickListener(this);
    layout.setOnCardLongClickListener(this);
```` 

You can `setOnCardClickListener` and `setOnCardLongClickListener`. 

## Contribution

Open to any kind of new idea, development suggestion or bug fixing. And If anyone want to contribute , I will appreciate. It is enough to just create a new PR that explaining problem and solution.

### Developed By

Muhammed Emin Ayar - ayarmuhammedemin@gmail.com

### Thanks

Inspired by [ObservableScrollView] (https://github.com/ksoichiro/Android-ObservableScrollView)

### License

```
Copyright 2017 Muhammed Emin AYAR

The MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
