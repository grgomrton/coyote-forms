import { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Link from '@mui/material/Link';
import AlertBar from './AlertBar';

function TriangleForm() {
    const [alpha, setAlpha] = useState(0);
    const [beta, setBeta] = useState(0);
    const [gamma, setGamma] = useState(0);
    const [alertConfiguration, setAlertConfiguration] = useState({ isOpen: false, success: false, successMessage: "", errors: [] })

    const collectInputValues = () => { return { "alpha": alpha, "beta": beta, "gamma": gamma } }

    const isValid = (testedFieldId) =>
        alertConfiguration.errors.filter((error) => error.fieldId === testedFieldId).length === 0;

    const saveLocation = (event) => {
      axios.post('http://localhost:8080/api/forms/triangle-form', collectInputValues())
          .then((response) =>
              setAlertConfiguration({isOpen: true, success: true, successMessage: "Triangle angles set", errors: []}))
          .catch((error) =>
              setAlertConfiguration({isOpen: false, success: false, errors: error.response.data.invalidFields }))
    }

    return (
      <div>
        <AlertBar {...alertConfiguration} />

        <TextField
            label="Alpha"
            id="angle-alpha"
            error={!isValid("alpha")}
            value={alpha}
            onChange={(event) => setAlpha(event.target.value)}
            sx={{ m: 1, minWidth: 120 }}
            inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        />
        <TextField
            label="Beta"
            id="angle-beta"
            error={!isValid("beta")}
            value={beta}
            onChange={(event) => setBeta(event.target.value)}
            sx={{ m: 1, minWidth: 120 }}
            inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        />
        <TextField
            label="Gamma"
            id="angle-gamma"
            error={!isValid("gamma")}
            value={gamma}
            onChange={(event) => setGamma(event.target.value)}
            sx={{ m: 1, minWidth: 120 }}
            inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        />

        <Button sx={{ m: 2, minWidth: 100 }} variant="contained" onClick={saveLocation}>Save</Button>

        <Link href="TriangleFormRuleSet.json" target="blank" rel="noopener">Show ruleset</Link> (*)
        <br />
        <Typography sx={{ m: 2 }} variant="subtitle1">
            * Part of the validation takes place using javax validation on the backend. See readme for details
        </Typography>
      </div>
    );
}

export default TriangleForm;