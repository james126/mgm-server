const form = document.getElementById('contact-form');

if (form != null){
    form.addEventListener('submit', submitForm);

    /**
     * Check form validity and POST form data to server
     * @param event form submission
     */
    function submitForm(event) {
        event.preventDefault();     //no request is sent to server

        if (!form.checkValidity()) {    //checkValidity is built in browser method
            form.classList.add('was-validated');    //adds invalid feedback
        } else {
            const formData = new FormData(event.target);
            const formObj = Object.fromEntries(formData.entries());
            const url = window.location.toString().substring(0,(window.location.toString().length)-8) + "/form"

            fetch(url, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'POST',
                body: JSON.stringify(formObj)
            }).then(function (response) {
                return response.status === 200
            }).then(function (response) {
                confirmSubmission();
            }).catch(function (error) {
                console.log('Request failed', error);
            });
        }
    }

    /**
     * When server response is OK, disable form elements and display confirmation message
     */
    function confirmSubmission() {
        //disable form elements
        const input = document.getElementsByTagName('input');
        const array = Array.from(input);
        Array.from(input).forEach(element => element.disabled = true);

        document.getElementById('textarea').disabled = true;
        document.getElementById('submit-button').disabled = true;

        //display confirmation message
        const paragraph = document.createElement("p");
        paragraph.classList.add('text-center', 'lead', 'submitted-text', 'font-weight-bold');
        paragraph.innerText = 'Thanks, we will be in touch shortly!';

        const brk = document.createElement("br");
        paragraph.appendChild(brk);

        const horizontal = document.getElementsByTagName("hr");
        form.insertBefore(paragraph, form.children[0]);
    }
}
