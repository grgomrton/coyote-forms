import Alert from '@mui/material/Alert';

function AlertBar(alertConfiguration) {
   if (alertConfiguration.isOpen) {
     if (alertConfiguration.success) {
       return (<Alert severity="success">Location saved</Alert>);
     }
   }
   return (<div></div>);
}

export default AlertBar;