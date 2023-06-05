/* CONTACT
checking form validity*/

var form=document.querySelector('.needs-validation');

form.addEventListener('submit', function(event){
    if(form.checkValidity() === false) {     //checkValidity built in browser method
        event.preventDefault();             //no request is sent to server
        event.stopPropagation();
    }
    form.classList.add('was-validated');
})
