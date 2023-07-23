import logo from './logo.svg';
import './App.css';
import { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Alert from '@mui/material/Alert';
import Link from '@mui/material/Link';
import Box from '@mui/material/Box';

      function AlertBar(alertConfiguration) {
        if (alertConfiguration.isOpen) {
          if (alertConfiguration.success) {
            return (<Alert severity="success">Location saved</Alert>);
          }
        }
        return (<div></div>);
      }

      function App() {
        const [permittedRegions, setPermittedRegions] = useState([]);
        const [selectedRegion, setSelectedRegion] = useState("");
        const [permittedCountries, setPermittedCountries] = useState([]);
        const [selectedCountry, setSelectedCountry] = useState("");
        const [permittedCities, setPermittedCities] = useState([]);
        const [selectedCity, setSelectedCity] = useState("");
        const [alertConfiguration, setAlertConfiguration] = useState({ isOpen: false, success: false, errors: [] })

        // we bind the validator to the three state variables
        const collectInputValues = () => { return { "region": selectedRegion, "country": selectedCountry, "city": selectedCity } }

        useEffect(() => { axios.post('http://localhost:8080/api/forms/location-form/inputs/region/permitted-values', collectInputValues())
             .then((response) => setPermittedRegions(response.data))
             .catch((error) => console.error(error))
          }, []);

        useEffect(() => { axios.post('http://localhost:8080/api/forms/location-form/inputs/country/permitted-values', collectInputValues())
             .then((response) => setPermittedCountries(response.data))
             .catch((error) => console.error(error))
          }, [selectedRegion]);

        useEffect(() => { axios.post('http://localhost:8080/api/forms/location-form/inputs/city/permitted-values', collectInputValues())
             .then((response) => setPermittedCities(response.data))
             .catch((error) => console.error(error))
          }, [selectedRegion, selectedCountry]);

        const handleChangeOfRegion = (event) => setSelectedRegion(event.target.value);
        const handleChangeOfCountry = (event) => setSelectedCountry(event.target.value);
        const handleChangeOfCity = (event) => setSelectedCity(event.target.value);

        const isValid = (testedFieldId) =>
            alertConfiguration.errors.filter((error) => error.fieldId === testedFieldId).length === 0;

        const saveLocation = (event) => {
          axios.post('http://localhost:8080/api/forms/location-form', collectInputValues())
              .then((response) =>
                  setAlertConfiguration({isOpen: true, success: true, errors: []}))
              .catch((error) =>
                  setAlertConfiguration({isOpen: false, success: false, errors: error.response.data.invalidFields }))
        }

        return (
          <div>
            <Box sx={{ p: 2, border: '1px solid primary.dark' }}  variant="button">
              Examples:
              <Link sx={{ m: 1 }} variant="button" href="/">Location</Link>
              <Link sx={{ m: 1 }} variant="button" href="/triangle-form.html">Triangle</Link>
            </Box>

            <AlertBar {...alertConfiguration} />
            <FormControl sx={{ m: 1, minWidth: 150 }} error={!isValid("region")}>
              <InputLabel id="region-label">Region</InputLabel>
              <Select
                labelId="region-label"
                id="region-select"
                value={selectedRegion}
                label="Region"
                onChange={handleChangeOfRegion}
              >
                { permittedRegions.map((region) => <MenuItem key={region} value={region}>{region}</MenuItem>) }
              </Select>
            </FormControl>
            <FormControl sx={{ m: 1, minWidth: 150 }} error={!isValid("country")}>
              <InputLabel id="country-label">Country</InputLabel>
              <Select
                labelId="country-label"
                id="country-select"
                value={selectedCountry}
                label="Country"
                onChange={handleChangeOfCountry}
              >
                { permittedCountries.map((country) => <MenuItem key={country} value={country}>{country}</MenuItem>) }
              </Select>
            </FormControl>
            <FormControl sx={{ m: 1, minWidth: 150 }} error={!isValid("city")}>
              <InputLabel id="city-label">City</InputLabel>
              <Select
                labelId="city-label"
                id="city-select"
                value={selectedCity}
                label="City"
                onChange={handleChangeOfCity}
              >
                { permittedCities.map((city) => <MenuItem key={city} value={city}>{city}</MenuItem>) }
              </Select>
            </FormControl>
            <Button sx={{ m: 2, minWidth: 100 }} variant="contained" onClick={saveLocation}>Save</Button>
            <Link href="LocationFormRuleSet.json" target="blank" rel="noopener">Show ruleset</Link>
          </div>
        );
      }

export default App;
