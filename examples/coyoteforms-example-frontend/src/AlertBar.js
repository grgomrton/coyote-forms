import Alert from '@mui/material/Alert';

function AlertBar(alertConfiguration) {
   if (alertConfiguration.isOpen) {
     if (alertConfiguration.success) {
       return (<Alert sx={{ mb: 2 }} severity="success">{alertConfiguration.successMessage}</Alert>);
     } else {
        return alertConfiguration.errors.map((failure, counter) => (<Alert key={counter} sx={{ mb: 1, mt: 0 }} severity="info">{failure}</Alert>));
     }
   }
   return (<div></div>);
}

export default AlertBar;