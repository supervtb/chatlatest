'use strict';

var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;



function connect(event, us) {
    username = us;
 if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
     stompClient.connect({}, onConnected, onError);
 }

}

var re

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
       re =  stompClient.subscribe("/topic/private", onMessageFromPrivate);
        stompClient.send("/app/chat.getMes");
}




function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);


    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    }
    else {
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender.concat());
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);



    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;


}

var arrayofusername;
var arrayofmessages;


function onMessageFromPrivate(payload) {
    var payloadbody = JSON.parse(payload.body)
    var payloadbodycontent = JSON.parse(payloadbody.content);
     arrayofusername = payloadbodycontent.map(function(data) {
       return data.username;
    });
    arrayofmessages = payloadbodycontent.map(function(data) {
        return data.message;
    });

    console.log(arrayofusername);
    console.log(arrayofmessages);

   bla(arrayofusername,arrayofmessages);


    re.unsubscribe();

}

function bla(arrayofusername,arrayofmessages ) {
    for(var i = 0; i< arrayofusername.length; i++) {
        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(arrayofusername[i].concat());
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);


        var textElement = document.createElement('p');
        var messageText = document.createTextNode(arrayofmessages[i]);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;


    }




}




messageForm.addEventListener('submit', sendMessage, true)


