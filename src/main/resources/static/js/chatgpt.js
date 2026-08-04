
function sendMessage(){

    let input=document.getElementById("question");

    let text=input.value.trim();

    if(text==="") return;

    let chat=document.getElementById("chat");

    /* User */

    chat.innerHTML+=`
<div class="message user">
<div class="bubble">${text}</div>
</div>`;

    /* Typing */

    chat.innerHTML+=`
<div class="message ai" id="typing">
<div class="bubble">
<div class="typing">
<span></span>
<span></span>
<span></span>
</div>
</div>
</div>`;

    chat.scrollTop=chat.scrollHeight;

    input.value="";

    /* Fake AI */

    setTimeout(()=>{

        document.getElementById("typing").remove();

        chat.innerHTML+=`
<div class="message ai">
<div class="bubble">
You asked:<br><br>
<b>${text}</b><br><br>
This is a demo response. Connect this input with your Spring Boot controller or AI API (OpenAI/Gemini) to generate real responses.
</div>
</div>`;

        chat.scrollTop=chat.scrollHeight;

    },1500);

}

/* Enter key */

document.getElementById("question").addEventListener("keypress",function(e){

    if(e.key==="Enter"){
        sendMessage();
    }

});
