import { createStyles, lighten, makeStyles, Theme } from '@material-ui/core/styles';

const drawerWidth = 160;

const useStyles = makeStyles((theme: Theme) => createStyles({
  root: {
    display: 'flex',
    flexGrow: 1,
    width: '100%',
  },
  page: {
    background: '#f9f9f9',
    width: '100%'
  },
  drawer: {
    width: drawerWidth
  },
  drawerPaper: {
    width: drawerWidth
  },
  drawerItemActive: {
    background: '#e4e4e4'
  },
  drawerItemInactive: {
    background: theme.palette.background.paper
  },
  toolBar: {
    width: `calc(100% -${drawerWidth}px)`,
    marginLeft: drawerWidth,
  },
  toolbarIcon: {
    marginRight: theme.spacing(2),
  },
  toolbarTitle: {
    flexGrow: 1,
  },
  container: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(1, 0, 1)
  },
  wrapper: {
    margin: theme.spacing(1),
    position: 'relative',
  },
  button: {
    margin: theme.spacing(3),
  },
  buttonProgress: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    marginTop: -12,
    marginLeft: -12,
  },
  formControl: {
    margin: theme.spacing(1),
    // minWidth: 150
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
  paper: {
    width: '100%',
    marginBottom: theme.spacing(2),
  },
  table: {
    minWidth: 750,
  },
  tableRow:
  {
    "&.Mui-selected, &.Mui-selected:hover": 
    theme.palette.type === 'light'
    ? {
      color: theme.palette.primary.main,
      backgroundColor: lighten(theme.palette.primary.light, 0.85),
      "& > .MuiTableCell-root": {
        color: "black"
      }
    }
    : {
      color: theme.palette.text.primary,
      backgroundColor: theme.palette.secondary.dark,
      "& > .MuiTableCell-root": {
        color: "white"
      }
    }
  },
  // tableRow: 
  // {
  //   "&.Mui-selected, &.Mui-selected:hover": {
  //     color: theme.palette.primary.main,
  //     backgroundColor: lighten(theme.palette.primary.light, 0.85),
  //     "& > .MuiTableCell-root": {
  //       color: "black"
  //     }
  //   }
  // },
  visuallyHidden: {
    border: 0,
    clip: 'rect(0 0 0 0)',
    height: 1,
    margin: -1,
    overflow: 'hidden',
    padding: 0,
    position: 'absolute',
    top: 20,
    width: 1,
  },
  textarea: {
    backgroundColor: theme.palette.background.paper,
    width: '600px'
  },
}));

export default useStyles;