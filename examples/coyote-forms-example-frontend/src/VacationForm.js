import { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';
import dayjs from 'dayjs';
import Link from '@mui/material/Link';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import AlertBar from './AlertBar';

function VacationForm() {
    const [startDate, setStartDate] = useState(dayjs());
    const [endDate, setEndDate] = useState(dayjs());
    const [alertConfiguration, setAlertConfiguration] = useState({ isOpen: false, success: false, errors: [] })

    const collectInputValues = () => { return { "startDate": startDate, "endDate": endDate } }

    const localizeInputId = (inputId) => {
        switch (inputId) {
            case "startDate": return "Start Date";
            case "endDate": return "End Date";
        }
    }
    const localizeErrorMessage = (message) => {
        switch (message) {
            case "INTERVAL_UP_TO_THREE_DAYS_RULE": return "Up to three days the request has to be sent one week before";
            case "INTERVAL_MORE_THAN_THREE_DAYS_RULE": return "For intervals more than three days the request has to be sent two weeks before";
        }
    }

    const localizeValidationResult = (failures) => failures.map((failure) => localizeInputId(failure.fieldId) + ': ' + localizeErrorMessage(failure.reason))

    const saveDate = (event) => {
      axios.post('http://localhost:8080/api/forms/vacation-form', collectInputValues())
          .then((response) =>
              setAlertConfiguration({isOpen: true, success: true, successMessage: "Request sent", errors: []}))
          .catch((error) =>
              setAlertConfiguration({isOpen: true, success: false, errors: localizeValidationResult(error.response.data.invalidFields) }))
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <AlertBar {...alertConfiguration} />

            <DatePicker
                label={localizeInputId("startDate")}
                sx={{ m: 1 }}
                value={startDate}
                onChange={(date) => setStartDate(date)}/>
            <DatePicker
                label={localizeInputId("endDate")}
                sx={{ m: 1 }}
                value={endDate}
                onChange={(date) => setEndDate(date)}/>

            <Button sx={{ m: 2, minWidth: 100 }} variant="contained" onClick={saveDate}>Save</Button>
            <Link href="http://localhost:8080/VacationFormRuleSet.json" target="blank" rel="noopener">Show ruleset</Link>
        </LocalizationProvider>
    );
}

export default VacationForm;