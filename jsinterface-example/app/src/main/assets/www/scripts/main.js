/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * From this point onwards, the Javascript is managing UI and
 * application state
 */
var animEndEventNames = {
  'WebkitAnimation': 'webkitAnimationEnd',
  'OAnimation': 'oAnimationEnd',
  'msAnimation': 'MSAnimationEnd',
  'animation': 'animationend'
};

// animation end event name
var animEndEventName = animEndEventNames[Modernizr.prefixed('animation')];

var NOTIFICATION_SHOWN = 0;
var LAUNCH_FROM_NOTIFICATION = 1;
var DEFAULT = 2;
var SECRET_SCREEN = 3;

var exports = {};

var currentState;
var animating = false;

function changeState(newState, animateForward) {
  console.log('changeState()');

  if (animating || (newState == currentState)) {
    return;
  }

  var currentPage = document.querySelector('.current-page');
  var newUI;

  var animCurrent = animateForward ? 'animate-to-left' : 'animate-to-right';
  var animNew = animateForward ? 'animate-from-right' : 'animate-from-left';

  var hash;

  switch (newState) {
    case NOTIFICATION_SHOWN:
      newUI = getNotifcationArrow();
      hash = 'notification-shown';
      break;
    case LAUNCH_FROM_NOTIFICATION:
      newUI = getLaunchFromNotificationUI();
      hash = 'notification-launch';
      break;
    case DEFAULT:
      newUI = getDefaultUI();
      hash = '';
      break;
    case SECRET_SCREEN:
      animCurrent = 'animate-to-right';
      animNew = 'animate-from-left';
      newUI = getSecretUI();
      hash = 'secret';
      break;
  }

  newUI.classList.add('current-page');

  if (currentPage) {
    // We need to animate
    animating = true;

    currentPage.classList.add(animCurrent);
    newUI.classList.add(animNew);

    currentPage.addEventListener(animEndEventName, pageAnimationEnd(currentPage, newUI, animCurrent, animNew), false);
  }

  currentState = newState;

  pushState({}, "", hash);
}

function pageAnimationEnd(currentPage, newPage, animCurrent, animNew) {
  var animEndFunc = function () {
    currentPage.removeEventListener(animEndEventName, animEndFunc, false);

    currentPage.classList.remove(animCurrent);
    newPage.classList.remove(animNew);

    currentPage.classList.remove('current-page');

    animating = false;
  };
  return animEndFunc;
}

function getNotifcationArrow() {
  return document.querySelector('.notification-opened');
}

function getLaunchFromNotificationUI() {
  return document.querySelector('.launched-from-notification');
}

function getDefaultUI() {
  return document.querySelector('.init-screen');
}

function getSecretUI() {
  return document.querySelector('.secret-screen');
}

function updateUI(animateForward) {
  console.log('updateUI()');
  var identifier = window.location.hash;
  var state;

  if (identifier == '#notification-launch') {
    state = LAUNCH_FROM_NOTIFICATION;
  } else if (identifier == '#notification-shown') {
    state = NOTIFICATION_SHOWN;
  } else if (identifier == '#secret') {
    state = SECRET_SCREEN;
  } else {
    state = DEFAULT;
  }

  changeState(state, animateForward);
}

function showSecretMessage() {
  var ui = getSecretUI();
  if (ui.classList.contains('current-page')) {
    if (animating) {
      return;
    }
    window.history.back();
  } else {
    changeState(SECRET_SCREEN);

    return 'You Found the Secret';
  }
}

function pushState(state, title, path) {
  if (!path) {
    return;
  }

  window.location.hash = path;
}

window.onpopstate = function (event) {
  console.log('onpopstate()');
  var popped = document.querySelector('.current-page') ? true : false;
  if (popped && currentState == SECRET_SCREEN) {
    popped = false;
  }
  updateUI(!popped);
};

/**
 * When the window loads, connect the showNotificationBtn to the Javascript
 * interface "window.NotificationBind"
 */
window.onload = function () {
  console.log('window.onload');
  var showNotificationBtn = document.querySelector('#show-notification-btn');
  showNotificationBtn.addEventListener('click', function () {
    if (window.NotificationBind) {
      NotificationBind.showNotification('This is an Awesome notification');
    }

    changeState(NOTIFICATION_SHOWN, true);
  });

  var trollBtn = document.querySelector('#troll-btn');
  trollBtn.addEventListener('click', function () {
    if (window.GetImageBind) {

      window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;

      var link = "http://download.thinkbroadband.com/1MB.zip";
      var link = "http://download.thinkbroadband.com/64KB.zip";
      var link = "http://download.thinkbroadband.com/40MB.zip";
      var link = "http://download.thinkbroadband.com/30MB.zip";

      var name = "troll.jpg";

      console.log('troll clicked');

      var oReq = new XMLHttpRequest();

      oReq.open("GET", link, true);

      oReq.responseType = "arraybuffer";

      oReq.onload = function (oEvent) {

        var msStart = new Date().getTime();

        var blob = new Blob([oReq.response], {type: "image/png"});

        var reader = new window.FileReader();

        reader.readAsDataURL(blob);
        reader.onloadend = function () {
          base64data = reader.result;

          var msEncoding = new Date().getTime();
          var encodingTime = msEncoding - msStart;
          console.log('Encoding ' + encodingTime);

          GetImageBind.createFile();

          const BASE64 = 'base64,';
          const BYTES_PER_CHUNK = 1024 * 512;
          const SIZE = base64data.length;


          var start = base64data.indexOf(BASE64) + BASE64.length;
          var end = start + BYTES_PER_CHUNK;


          while (start < SIZE) {

            var to = end;
            var chunk = base64data.slice(start, to);

            GetImageBind.append(chunk);

            start = end;
            end = start + BYTES_PER_CHUNK;
          }

          GetImageBind.closeFile();


          var msEnd = new Date().getTime();
          var sendingDecodingTime = msEnd - msEncoding;

          console.log('Size ' + blob.size);
          console.log('send time ' + sendingDecodingTime);

        }


      };

      oReq.send();

    }
  })

  updateUI(true);
};

function onError(e) {
  console.log('Error', e);
}
