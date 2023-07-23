import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';

function AlertBar(alertConfiguration) {
   if (alertConfiguration.isOpen) {
     if (alertConfiguration.success) {
       return (<Alert sx={{ mb: 2}} severity="success">{alertConfiguration.successMessage}</Alert>);
     }
   }
   return (<div></div>);
}

export default AlertBar;