import Box from '@mui/material/Box';
import Link from '@mui/material/Link';

function Header() {
    return (<Box sx={{ p: 2, border: '1px solid primary.dark' }}  variant="button">
                Examples:
                <Link sx={{ m: 1 }} variant="button" href="/">Location</Link>
                <Link sx={{ m: 1 }} variant="button" href="/triangle-form.html">Triangle</Link>
            </Box>);
}

export default Header;